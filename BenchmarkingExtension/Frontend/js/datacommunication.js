/**
 * Represents user data and related functionality.
 */
UserData = {
    origin: null,
    subjectID: null,
    isMobileDevice: undefined,
    camera: {},
    cores: undefined,
    agent: undefined,
    language: undefined,
    feedback: undefined,

    init: function () {
        //load cookie if exists
        let sID = Util.getCookie("subjectID");
        if(sID !== null){
            this.assignSubjectID(sID);
        }

        //collect technical details
        UserData.isMobileDevice = this.checkIfMobile();

        //get origin variable
        UserData.origin = document.getElementById("origin").innerText;
    },

    getTechnicalData: async function () {
        //camera
        await navigator.mediaDevices.enumerateDevices()
            .then(function(devices) {
                devices.forEach(function(device) {
                    if(device.label !== null && device.kind === "videoinput"){      //grab with a label = has been granted access
                        UserData.camera["cameraLabel"] = (' ' + device.label.toString()).slice(1); //bugfix, deep copy to retain string

                    }
                });
            })
            .catch(function(err) {
                //do nothing
            });

        const vid = document.getElementById("webgazerVideoFeed");
        let vidSettings = vid.srcObject.getVideoTracks()[0].getSettings();

        UserData.camera["frameRate"] = vidSettings.frameRate;
        UserData.camera["height"] = vidSettings.height;
        UserData.camera["width"] = vidSettings.width;

        //agent
        UserData.agent = navigator.userAgent;

        //Cores
        UserData.cores = navigator.hardwareConcurrency;

        //Language of computer
        UserData.language = navigator.language;

        //send data
        ServerCommunication.sendTechnicalData();
    },

    assignSubjectID: function (subID) {
        this.subjectID = subID;
        Util.setCookie("subjectID", this.subjectID);
    },

    checkIfMobile: function () {
        return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
    }
};

/**
 *  Implementation of client-server communication for an experiment. The ServerCommunication object includes
 *  several methods for sending and receiving data.
 */
ServerCommunication = {
    asyncRequest: undefined,

    sendQuestionnaire: function () {
        //data object
        let demogData = {};

        //collect form data
        const formData = new FormData(document.querySelector('form'));
        for (let pair of formData.entries()) {
            demogData[pair[0]] = pair[1];
        }

        demogData["origin"] = UserData.origin;

        this.newRequest();

        //compose request URL
        let requestUrl =  'dataCollection.php?dataType=demographics';

        // set up callback function
        this.asyncRequest.addEventListener("readystatechange",function() { ServerCommunication.questionnaireSubmitted(); }, false);

        // send the asynchronous request
        this.asyncRequest.open("POST", requestUrl, true);
        this.asyncRequest.setRequestHeader("Content-Type","application/json; charset=utf-8" );
        this.asyncRequest.send(JSON.stringify(demogData));
    },

    questionnaireSubmitted: function () {
        if (this.asyncRequest.readyState === XMLHttpRequest.DONE && this.asyncRequest.status === 200) {
            this.asyncRequest.removeEventListener("readystatechange",ServerCommunication.questionnaireSubmitted, false);

            let resp = JSON.parse(this.asyncRequest.responseText);
            UserData.assignSubjectID(resp);

            //do the example
            CodeDisplay.display("example");
        }
    },

    requestNextSnippet: function () {
        this.newRequest();

        //request next snippet
        this.asyncRequest.addEventListener("readystatechange",function() { ServerCommunication.processNextSnippet(); }, false);

        // send the asynchronous request
        let requestUrl = 'dataCollection.php?request=ballTracing&subID=' + UserData.subjectID;
        this.asyncRequest.open( "GET", requestUrl, true );
        this.asyncRequest.setRequestHeader("Accept","application/json; charset=utf-8" );
        this.asyncRequest.send();
        // }
    },

    processNextSnippet: function () {
        if (this.asyncRequest.readyState === XMLHttpRequest.DONE && this.asyncRequest.status === 200) {

            CodeDisplay.allShown = true;
            ServerCommunication.getResult();

        }
    },

    sendBallCoordinateData: function (jsonData) {
        GazeDataCollection.pauseEyeData();
        let data = {};

        // const jsonDataMod = jsonData.replace(/\\/g, '');
        console.log("JSON DATA: ");
        console.log(jsonData);
        data["ballPosition"] = JSON.parse(jsonData);

        //get dimensions
        let dims = {};
        let rect = Navigation.content.getBoundingClientRect();
        dims["x"] = Util.roundToTwo(rect.left);
        dims["y"] = Util.roundToTwo(rect.top);
        dims["h"] = Util.roundToTwo(Navigation.content.offsetHeight);
        dims["w"] = Util.roundToTwo(Navigation.content.offsetWidth);
        data["dimensions"] = dims;

        //compose request URL
        this.newRequest();
        let requestUrl =  'dataCollection.php?dataType=ballCoordinate&subID=' + UserData.subjectID;

        this.asyncRequest.addEventListener("readystatechange", function() { ServerCommunication.ballCoordinateDataSent(); }, false);

        // send the asynchronous request
        this.asyncRequest.open("POST", requestUrl, true);
        this.asyncRequest.setRequestHeader("Content-Type","application/json; charset=utf-8" );
        console.log(data);
        this.asyncRequest.send(JSON.stringify(data));
    },


    ballCoordinateDataSent: function () {
        if (this.asyncRequest.readyState === XMLHttpRequest.DONE && this.asyncRequest.status === 200) {
            this.asyncRequest.removeEventListener("readystatechange", ServerCommunication.ballCoordinateDataSent, false);
            CodeDisplay.allShown = true;
            document.cookie = "subjectID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        }
    },

    sendEyeOnDotPositionData: function () {
        GazeDataCollection.pauseEyeData();

        let data = {};
        let gazeData = GazeDataCollection.gazeData;

        data["gazeData"] = gazeData;

        //get dimensions
        let dims = {};
        let rect = Navigation.content.getBoundingClientRect();
        dims["x"] = Util.roundToTwo(rect.left);
        dims["y"] = Util.roundToTwo(rect.top);
        dims["h"] = Util.roundToTwo(Navigation.content.offsetHeight);
        dims["w"] = Util.roundToTwo(Navigation.content.offsetWidth);
        data["dimensions"] = dims;
        //compose request URL
        this.newRequest();
        let requestUrl =  'dataCollection.php?dataType=ballTracing&subID=' + UserData.subjectID;

        this.asyncRequest.addEventListener("readystatechange", function() { ServerCommunication.eyeOnDotPositionDataSent(); }, false);

        // send the asynchronous request
        this.asyncRequest.open("POST", requestUrl, true);
        this.asyncRequest.setRequestHeader("Content-Type","application/json; charset=utf-8" );
        this.asyncRequest.send(JSON.stringify(data));
    },

    eyeOnDotPositionDataSent: function () {
        if (this.asyncRequest.readyState === XMLHttpRequest.DONE && this.asyncRequest.status === 200) {
            this.asyncRequest.removeEventListener("readystatechange", ServerCommunication.eyeOnDotPositionDataSent, false);
            //All done
            CodeDisplay.allShown = true;
            //end of test
            document.cookie = "subjectID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        }
    },

    sendCalibrationData: function () {
        GazeDataCollection.pauseEyeData();
        this.newRequest();

        //collect data
        let data = {};
        data["calibData"] = GazeDataCollection.validationReads;

        //get dimensions
        let dims = {};
        let rect = Navigation.content.getBoundingClientRect();
        dims["x"] = Util.roundToTwo(rect.left);
        dims["y"] = Util.roundToTwo(rect.top);
        dims["h"] = Util.roundToTwo(Navigation.content.offsetHeight);
        dims["w"] = Util.roundToTwo(Navigation.content.offsetWidth);
        data["dimensions"] = dims;

        //compose request URL
        let requestUrl =  'dataCollection.php?dataType=calibration&subID=' + UserData.subjectID;

        this.asyncRequest.addEventListener("readystatechange", function() { ServerCommunication.getCalibrationPrecision(); }, false);

        // send the asynchronous request
        this.asyncRequest.open("POST", requestUrl, true);
        this.asyncRequest.setRequestHeader("Content-Type","application/json; charset=utf-8" );
        this.asyncRequest.send(JSON.stringify(data));

    },

    getCalibrationPrecision: function () {
        if (this.asyncRequest.readyState === XMLHttpRequest.DONE && this.asyncRequest.status === 200) {

            this.asyncRequest.removeEventListener("readystatechange", ServerCommunication.getCalibrationPrecision, false);

            let resp = JSON.parse(this.asyncRequest.responseText);

            //response contains average precision in px.
            //check threshold
            let threshold = Calibration.areaHeight / 3;

            //For debugging show error
            //alert(resp + 'px');

            if(resp > threshold){
                //did not pass
                Calibration.isCalibrated = false;
                Calibration.failedCalibrations++;

                if(Calibration.failedCalibrations < 4){
                    MessagePrompt.showMessage('Calibration failed to pass the accuracy threshold and you will need to do some further calibration.' +
                        ' Please make sure you follow the directions when you recalibrate.','Recalibrate', function(){
                        //Do full calibration.
                        VideoInstructions.showInstructions(false);
                    });
                }else{
                    MessagePrompt.showMessage('Unfortunately your calibrations has been unsuccessful and you will not be able to continue the experiment. ' +
                        'It is possible your hard/software set-up is not suited for the task. Thank you for your participation, the data generated so far will still be helpful.',
                        'Ok', function () {
                            ServerCommunication.getResult();
                        });
                }
            }else{
                //continue to show code snippet
                Calibration.failedCalibrations = 0;
                Calibration.isCalibrated = true;
                ServerCommunication.requestNextSnippet();

            }
        }
    },

    sendEyeData: function (snippet, answer) {
        GazeDataCollection.pauseEyeData();

        let data = {};
        data["snippet"] = snippet;
        data["answer"] = answer;
        data["gazeData"] = GazeDataCollection.gazeData;

        //get dimensions
        let dims = {};
        let img = document.getElementById("snippet");
        let rect = img.getBoundingClientRect();
        dims["x"] = Util.roundToTwo(rect.left);
        dims["y"] = Util.roundToTwo(rect.top);
        dims["h"] = Util.roundToTwo(img.offsetHeight);
        dims["w"] = Util.roundToTwo(img.offsetWidth);

        data["dimensions"] = dims;

        //compose request URL
        this.newRequest();
        let requestUrl =  'dataCollection.php?dataType=answer&subID=' + UserData.subjectID;

        this.asyncRequest.addEventListener("readystatechange", function() { ServerCommunication.eyeDataSent(); }, false);

        // send the asynchronous request
        this.asyncRequest.open("POST", requestUrl, true);
        this.asyncRequest.setRequestHeader("Content-Type","application/json; charset=utf-8" );
        this.asyncRequest.send(JSON.stringify(data));

        //For debugging: report sample frequency.
        //alert(Util.roundToTwo(1000 / ((data["gazeData"][data["gazeData"].length - 1]["timeStamp"] - data["gazeData"][0]["timeStamp"]) / data["gazeData"].length)) + 'Hz');
    },

    eyeDataSent: function () {
        if (this.asyncRequest.readyState === XMLHttpRequest.DONE && this.asyncRequest.status === 200) {
            this.asyncRequest.removeEventListener("readystatechange", ServerCommunication.eyeDataSent, false);

            //reports back how many finished questions
            let resp = JSON.parse(this.asyncRequest.responseText);

            //redirect to next snippet
            if(resp === 3){
                //All done
                CodeDisplay.allShown = true;
                //end of test
                document.cookie = "subjectID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                ServerCommunication.getResult();
            }else{
                ServerCommunication.requestNextSnippet();
            }
        }
    },

    sendTechnicalData: function(){
        let techData = {};

        techData['Camera'] = UserData.camera;
        techData['Agent'] = UserData.agent;
        techData['Cores'] = UserData.cores;
        techData['Language'] = UserData.language;

        //compose request URL
        this.newRequest();
        let requestUrl =  'dataCollection.php?dataType=technical&subID=' + UserData.subjectID;

        this.asyncRequest.addEventListener("readystatechange", function() { ServerCommunication.techDataSent(); }, false);

        // send the asynchronous request
        this.asyncRequest.open("POST", requestUrl, true);
        this.asyncRequest.setRequestHeader("Content-Type","application/json; charset=utf-8" );
        this.asyncRequest.send(JSON.stringify(techData));
    },

    techDataSent: function(){
        if (this.asyncRequest.readyState === XMLHttpRequest.DONE && this.asyncRequest.status === 200) {
            this.asyncRequest.removeEventListener("readystatechange", ServerCommunication.techDataSent, false);

            let resp = JSON.parse(this.asyncRequest.responseText);

            //redirect to first calibration
            if(resp === "ok"){
                Calibration.calibrate(true);
            }
        }
    },

    getResult: function(){
        this.newRequest();

        //request next snippet
        this.asyncRequest.addEventListener("readystatechange",function() { ServerCommunication.processResult(); }, false);

        // send the asynchronous request
        let requestUrl = 'dataCollection.php?request=result&subID=' + UserData.subjectID;
        this.asyncRequest.open( "GET", requestUrl, true );
        this.asyncRequest.setRequestHeader("Accept","application/json; charset=utf-8" );
        this.asyncRequest.send();
    },

    processResult: function () {
        if (this.asyncRequest.readyState === XMLHttpRequest.DONE && this.asyncRequest.status === 200) {
            let resp = JSON.parse(this.asyncRequest.responseText);
            this.asyncRequest.removeEventListener("readystatechange",ServerCommunication.processResult, false);

            if(resp["done"] === 12 || Calibration.failedCalibrations === 4){
                //end of test
                document.cookie = "subjectID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                EndOfExperiment.hasEnded(resp["correct"], resp["done"]);
            }else{
                CodeDisplay.display(resp["showNext"]);
            }
        }
    },

    sendFeedBack: function(){
        this.newRequest();

        // send the asynchronous request
        let requestUrl = 'dataCollection.php?dataType=feedback&subID=' + UserData.subjectID;
        this.asyncRequest.open("POST", requestUrl, true);
        this.asyncRequest.setRequestHeader("Content-Type","application/json; charset=utf-8" );
        this.asyncRequest.send(UserData.feedback);
    },

    sendPhoto: function (data) {
        this.newRequest();

        // send the asynchronous request
        let requestUrl = 'dataCollection.php?dataType=photo&subID=' + UserData.subjectID;
        this.asyncRequest.open("POST", requestUrl, true);
        this.asyncRequest.setRequestHeader("Content-Type","application/json; charset=utf-8" );
        this.asyncRequest.send(data);
    },

    newRequest: function () {
        try
        {
            if (window.XMLHttpRequest)
            {
                // code for IE7+, Firefox, Chrome, Opera, Safari
                this.asyncRequest = new XMLHttpRequest();
            }
            else if (window.ActiveXObject)
            {
                // code for IE6, IE5
                this.asyncRequest = new ActiveXObject("Microsoft.XMLHTTP");
            }
            else
            {
                throw "Request Failed"
            }
        }
        catch (exception)
        {
            alert(exception.message);
        }
    }
};