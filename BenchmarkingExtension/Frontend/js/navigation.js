let ballSpeed = null;
let randomMoves = null;
let randomStart = null;
let exTime = null;
let backgroundImg = null;
let demo = 0;


/**
 * The Navigation represents the navigation flow of the eye-tracking test application. It initializes the required
 * div elements, connects buttons, and displays the instructions for the eye-tracking test, including the questionnaire,
 * calibration, and the actual test. It also includes a demo option for users to see how the test will look like.
 */
Navigation = {
    content: undefined,
    nav: undefined,
    txtDiv: undefined,

    init: function () {
        UserData.init();

        //identify divs
        this.content = document.getElementById("contentDiv");
        this.nav = document.getElementById("divButtons");
        this.txtDiv = document.getElementById("txtDiv");
        this.startTextDiv = document.getElementById("start-text");
        this.topicTextDiv = document.getElementById("topic-text");
        // EndOfExperiment.hasEnded("correct" , "done");
        //check if subjectID is set = ongoing experiment
        if(UserData.subjectID !== null){
            //show instructions
            VideoInstructions.showInstructions(true);
            return;
        }

        //connect the first button
        document.getElementById("btnPartake").addEventListener("click", function () {
            //check if mobile device
            if(UserData.isMobileDevice === true){
                MessagePrompt.showMessage("It appears you are using a mobile device. It is preferred you use a computer to undertake the experiment" +
                    " as it might not work properly otherwise. Thank you.", "Close", function () {
                    Navigation.showExpInstructions();
                });
                return;
            }
            Navigation.showExpInstructions();
        },false);
    },


    /**
     This method displays the instructions for the eye-tracking test, including the questionnaire, calibration,
     and the actual test. It also includes a demo option for users to see how the test will look like.
     */
    showExpInstructions: function () {
        JsonFileReading.readJsonData();
        Navigation.topicTextDiv.innerHTML = ' ';
        this.nav.innerHTML = '<button id="btnStart"  class="divButtons" style="display: margin: auto;">Begin</button>';
        document.getElementById("btnStart").addEventListener("click", function () {Questionnaire.showQuestionnaire(); }, false);


        this.txtDiv.innerHTML =
            '<p class="topic-text">How it works? \n' +
            '<div class="textBench">' +
            '<p>'+
            '<b>QUESTIONNAIRE: <br> </b> ' +
            ' Please fill out the questionnaire with requested data: <br>' +
            ' <hr> ' +
            ' <b>CALIBRATION: </b> <br> ' +
            ' Follow the instructions provided during calibration. The camera must be calibrated before the test. ' +
            ' During calibration, you should look at small circles with numbers inside that appear on the screen. ' +
            ' When the number reaches <b>0</b>, a new circle will appear in a different place on the screen. ' +
            ' Follow these points with your eyes until calibration is complete. This test is not long, ' +
            ' try to keep your head still during this experiment. This step is important to teach the algorithm ' +
            ' to correctly read the position of your eye pupil.' +
            ' <hr>' +
            '<b> START THE TEST: </b> <br>' +
            ' Follow the moving dot with your eyes. ' +
            '<hr>' +
            ' <b>IMPORTANT INFORMATION: </b> <br>' +
            ' Throughout the entire test, access to the camera is necessary. ' +
            ' The camera should be positioned at eye level for the user. ' +
            ' When the number reaches <b>0</b>, a new circle will appear in a different place on the screen. ' +
            ' <hr>' +
            ' <b>HEAD MOVEMENT: </b> <br>' +
            ' The only exception is a test that involves moving the head sideways. ' +
            ' In this case, direct your head towards the moving thick bar ' +
            ' at the top of the screen while reading the text from the designated location indicated by the dot. ' +
            '</p> <hr>' +
            '<b> START THE DEMO: </b> <br>' +
            ' This key starts the test demo. This way you can see how the test will look like. After the presentation, ' +
            'the program will return to the main page. ' +
            '<button id="btnStartDemo"  class="divButtons" style="display: margin: auto; border-radius: 10px;">Start Demo</button>' +
            '<hr>' +
            '<div style="display: flex; flex-direction: column; justify-content: center; align-items: center; ">' +
            '    <b>TO CONTINUE, PLEASE PRESS THE "BEGIN" BUTTON</b><br>' +
            '</div>' +
            '<img src="pictures/works.png" alt="Works Image" style="left: 300px; top: 0; noRepeat: true; padding-left: 10px;">'+
            '</div>';
            document.getElementById("btnStartDemo").addEventListener("click", function () {
            demo = 1;
            console.log("Start Demo: " + demo);
            renderBall.renderGreenBall();
        }, false);
    }
};

/**
This function reads data from a JSON file called "config.json" using an XMLHttpRequest object.
It handles the parsed JSON data and assigns it to global variables such as ballSpeed, exTime,
randomMoves, randomStart, backgroundImg, and desiredIntervalTime.
*/

JsonFileReading = {
    readJsonData: function () {

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                var response = JSON.parse(this.responseText);
                ballSpeed = response.ballSpeed;
                exTime = response.exTime;

                // Handle the string value for randomMove
                randomMoves = response.randomMoves;
                if (randomMoves === "yes") {
                    randomMoves = true;
                } else if (randomMoves === "no") {
                    randomMoves = false;
                } else {
                    // Handle unexpected value for randomMoves
                    console.error("Unexpected value for randomMove: " + randomMoves);
                }

                // Handle the string value for randomStart
                randomStart = response.randomStart;
                if (randomStart === "yes") {
                    randomStart = true;
                } else if (randomStart === "no") {
                    randomStart = false;
                } else {
                    // Handle unexpected value for randomStart
                    console.error("Unexpected value for randomStart: " + randomStart);
                }

                backgroundImg = response.backgroundImg;
                desiredIntervalTime = response.desiredIntervalTime;
            }
        };
        // Add a cache-busting query parameter to the URL
        var url = "config.json?" + new Date().getTime();
        xhttp.open("GET", url, true);
        xhttp.send();
    }
}



/**
 Questionnaire Benchmarking extension, represents a questionnaire form
 that collects personal information and optional notes from the user.
*/

Questionnaire = {
    showQuestionnaire: function () {
       Navigation.startTextDiv.innerHTML = 'Questionnaire';
       Navigation.topicTextDiv.innerHTML = ' ';
       Navigation.nav.innerHTML ='<button type="submit" form="demogForm" id="btnSubmit" class="divButtons">Calibration and test</button>';
       Navigation.txtDiv.innerHTML =
            '<h4 style="text-align: center; margin: 10px; padding: 10px; background-color: #f0f0f0; border: 0px solid #e96cab; border-radius: 5px; box-shadow: 0 0 5px rgb(229,1,1), 0 0 10px rgba(0, 0, 255, 0.3) inset;">PLEASE COMPLETE THE QUESTIONNAIRE (red boxes are mandatory):</h4>\n\n\n' +
            '<form id="demogForm" style="margin: 20px; padding: 20px; background-color: #fafafa;' +
            ' border: 1px solid #6cafe9; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 255, 0.5), 0 0 10px rgba(0, 0, 255, 0.3) inset;">\n' +
            '<h4 style="text-align: center; margin: 10px; padding: 10px; background-color: #f0f0f0; border: 0px solid #3bc9e3; border-radius: 5px; box-shadow: 0 0 5px rgb(24,16,234), 0 0 10px rgba(0, 0, 255, 0.3) inset;">PERSONAL DETAILS</h4>\n\n\n' +

            '          <p>\n' +
            '              <strong>Gender: </strong>\n' +
            '              <label><input name="gender" type="radio" value="female" checked style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6);"> Female</label>\n' +
            '              <label><input name="gender" type="radio" value="male" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6);"> Male</label>\n' +
            '              <label><input name="gender" type="radio" value="other" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6);"> Other</label>' +
            '          </p>\n' +
            '          <p>\n' +
            '              <strong>Age: </strong>\n' +
            '              <label><input id="ageTxt" name="age" type="text" size="2" autocomplete="off" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6); border-radius: 5px; font-size: 15px;  border: 3px solid red;">\n</label>\n' +
            '          </p>\n' +
            '          <p>\n' +
            '              <label><input name = "hasGlasses" type = "checkbox" value = "yes" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6);">I am wearing glasses</label>' +
            '          </p>' +
            '          <p>' +
            '              <label><input name = "hasEyeCond" type = "checkbox" value = "yes" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6); border-radius: 5px; font-size: 15px;">I have an eye condition eg. glaucoma, cataracts, amblyopia</label>' +
            '          </p>' +
            '          <p>' +
            '              <label><input name = "bigLashes" type = "checkbox" value = "yes" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6); border-radius: 5px; font-size: 15px;">I have eyelash extensions/thick mascara</label>' +
            '          </p>' +
            '      </div>\n' +
            '<p><label><strong>Optional notes: </strong><br><br/><textarea name = "comments" rows = "4" cols = "56" placeholder="ex. web camera model/specifications, laptop model"' +
            'style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6); font-size: 15px; border-radius: 5px;"></textarea></label></p>' +

           ' <p><label><strong style="color: #2E8B57; text-align: center; line-height: 1.5em;">In the next step, the camera will be calibrated, then you will immediately go to the test. <br> You should follow the ball which from the center of the screen ' +
           ' will start to move in different directions. ' +
           '<br> Try not to move your head and keep your eyes on the ball throughout the experiment.</label></p>\n' +
           '<strong style="color: darkred; font-family: Verdana, sans-serif;">Calibration time: </strong>\n' +  "13" + '<strong style="color: #663399;"> seconds</strong><br><br>\n' +
           '<strong style="color: darkred; font-family: Verdana, sans-serif;">Test time execution: </strong>\n' + exTime + '<strong style="color: #663399;"> seconds</strong><br><br>\n' +
           '<h4 style="text-align: center; margin: 10px; padding: 10px; background-color: #f0f0f0; color: navy; border: 0px solid #e96cab; border-radius: 5px; box-shadow: 0 0 5px rgb(229,1,1), 0 0 10px rgba(0, 0, 255, 0.3) inset;">CLICK "CALIBRATION AND TEST" BUTTON TO RUN EXPERIMENT -----></h4>\n' +
           ' <input id="ballSpeed" name="ballSpeed" value="' + ballSpeed + '" readonly size="0" style="color:#fafafa;border:0px;font-size:0px;">' +
           ' <input id="exTime" name="exTime" value="' + exTime + '" readonly size="0" style="color:#fafafa;border:0px;font-size:0px;">' +
           ' <input id="randomMoves" name="randomMoves" value="' + randomMoves + '" readonly size="0" style="color:#fafafa;border:0px;font-size:0px;">' +
           ' <input id="randomStart" name="randomStart" value="' + randomStart + '" readonly size="0" style="color:#fafafa;border:0px;font-size:0px;">' +
           ' <input id="backgroundImage" name="backgroundImage" value="' + backgroundImg + '" readonly size="0" style="color:#fafafa;border:0px;font-size:0px;">' +
           ' <input id="desiredIntervalTime" name="desiredIntervalTime" value="' + desiredIntervalTime + '" readonly size="0" style="color:#fafafa;border:0px;font-size:0px;">' +
           '</form>';


       //Disable button and force age validation
        document.getElementById("ageTxt").addEventListener("input", function () { Questionnaire.validateAge(); }, false);

        this.validateAge();

        //connect submit button
        document.getElementById("demogForm").setAttribute("action","javascript:ServerCommunication.sendQuestionnaire()");
    },

    validateAge: function () {
        let ageText = document.getElementById("ageTxt").value;

        if(!isNaN(ageText) && ageText > 0 && ageText < 100){
            document.getElementById("btnSubmit").disabled = false;
            return;
        }
        document.getElementById("btnSubmit").disabled = true;
    }
};

/**
 A collection of methods related to showing video instructions and setting up the camera for data collection.
 */
VideoInstructions = {
    setUpTO: undefined,

    /**
     Displays video instructions on the Navigation content and sets up the camera for data collection.
     @param firstTime a boolean indicating whether this is the first time the instructions are being displayed
     */
    showInstructions: function (firstTime) {
        Navigation.content.innerHTML =
            '    <h2>Camera setup</h2>\n' +
            '    <div id="videoDest" style="height: 50%">\n' +
            '        <h3 class="red" id="giveVideoPerm">Please give the browser webcam permissions</h3>\n' +
            '    </div>\n' +
            '    <h3>Please ensure you adhere to the following points to ensure accurate measurements</h3>\n' +
            '    <p class="noMargin">Tip: Resting your head on your hand might increase precision and comfort.</p>' +
            '    <img src="pictures/videoInstructionsAnim.png" alt="instructions" id="vidInst" width="auto" height="25%"><br/>';

        if(firstTime){
            Navigation.nav.innerHTML = '<button id="finishVidInst" class="divButtons" disabled>Calibrate</button>';
        }else{
            Navigation.nav.innerHTML = '<button id="finishVidInst" class="divButtons" disabled>Recalibrate</button>';
        }

        document.getElementById("finishVidInst").addEventListener("click", function () {
            //hide the camera in invisible div (faster than starting and stopping it)
            let video = document.getElementById("webgazerVideoContainer");
            document.getElementById("hidden").appendChild(video);

            //pause webgazer to catch up stack???
            GazeDataCollection.pauseEyeData();

            if(firstTime){
                //send the technical data to the server and re-navigate
                UserData.getTechnicalData();
            }else{
                Calibration.calibrate();
            }
        }, false);

        //turn on video
        if(!GazeDataCollection.isRunning){
            GazeDataCollection.startCollectingCameraData();
        }else{
            VideoInstructions.relocateVideo();
        }

        GazeDataCollection.resumeEyeData();
    },

    /**
     Relocates the video feed to the Navigation content and enables the "Finish" button when the camera is ready.
     */
    relocateVideo: async function () {
        let video = document.getElementById("webgazerVideoContainer");
        video.style.display = "block"; video.style.position = "relative"; video.style.margin = "auto";

        let vidDest = document.getElementById("videoDest");
        vidDest.innerHTML = '';
        vidDest.appendChild(video);


        document.getElementById("finishVidInst").disabled = false;

        //Set backup timer to check for failed set up
        this.setUpTO = setTimeout(function () {
            MessagePrompt.hidePrompt();

            if(!webgazer.isReady()){
                //Can not access the video feed
                MessagePrompt.showMessage("Failed to access video. Make sure you have a working camera and have given " +
                    "the browser permission at access it. Refresh the browser to try again or click the button to discontinue the " +
                    "experiment.","Discontinue", function () {
                    document.cookie = "subjectID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                    location.reload();
                });
            }else{
                //Can not find the eyes
                MessagePrompt.showMessage("Failed to locate both eyes in the video feed. Make sure the camera is working and has an " +
                    "unobstructed view of your face. Refresh the browser to try again or click the button to discontinue the " +
                    "experiment.","Discontinue", function () {
                    document.cookie = "subjectID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                    location.reload();
                });
            }
        }, 60000);

        this.checkIfReady();
    },

    /**
     This method checks if the user's face is ready for calibration.
     */
    checkIfReady: function(){
        let faceBox = document.getElementById("webgazerFaceFeedbackBox");
        if(faceBox === null || faceBox.style.borderColor !== 'green'){
            setTimeout(function () {
                VideoInstructions.checkIfReady();
            }, 100);
        }else{
            clearTimeout(this.setUpTO);
            MessagePrompt.hidePrompt();
        }
    }
};

/**
 Initializes the MessagePrompt object by retrieving and storing references to various HTML elements.
 Displays message prompts with the specified messages.
 Hides the message prompt and removes any click event listeners attached to the button.
 */
let MessagePrompt = {
    prompt: undefined,
    img: undefined,
    txt: undefined,
    btn: undefined,
    loader: undefined,
    isLoader: false,

     init: function () {
        this.prompt = document.getElementById("overlay");
        this.img = document.getElementById("promptImg");
        this.txt = document.getElementById("promptTxt");
        this.btn = document.getElementById("okBtn");
        this.loader = document.getElementById("loader");

        this.btn.addEventListener("click", function(){ MessagePrompt.hidePrompt() }, false);
    },

    showMessage: function (msg, btnTxt, doNext) {
        if(doNext !== null){
            this.btn.addEventListener("click",function(){doNext();}, false);
        }

        this.loader.style.display = "none";
        this.img.style.display = "none";

        this.txt.innerHTML = msg;
        this.btn.innerHTML = btnTxt;
        this.btn.style.display = "block";

        this.prompt.style.display = "block";
    },

    showImgMessage: function (msg, imgSrc, btnTxt, doNext) {
        this.img.src = imgSrc;
        this.img.style.display = "inline-block";

        if(doNext !== null){
            this.btn.addEventListener("click",function(){doNext();}, false);
        }

        this.loader.style.display = "none";

        this.txt.innerHTML = msg;
        this.btn.innerHTML = btnTxt;
        this.btn.style.display = "block";

        this.prompt.style.display = "block";
    },

    showLoader: function () {
        this.isLoader = true;
        this.prompt.style.display = "block";
        this.loader.style.display = "block";
        this.img.style.display = "none";
        this.btn.style.display = "none";
        this.txt.innerHTML = 'Setting up eye-tracking algorithm.' +
            '<br/>(make sure you have given the browser webcam access)' +
            '<br/> Please wait...';
    },

    hidePrompt: function() {
        this.isLoader = false;
        this.prompt.style.display = "none";

        //Regenerate btn to remove all listeners
        let newBtn = this.btn.cloneNode(true);
        this.btn.parentNode.replaceChild(newBtn, this.btn);
        this.btn = newBtn;
        this.btn.addEventListener("click", function(){ MessagePrompt.hidePrompt() }, false);
    }
};


/**
 The CodeDisplay handles the display of code snippets and experiment instructions.
 It handles the calibration part and the main experiment start.
 The method also contains a timeout function that is called when the experiment duration has elapsed.
 */
CodeDisplay = {
    snippetsShown: 0,
    dims: undefined,
    tO: undefined,
    allShown: false,
    display: function (snippet) {
        Navigation.content.innerHTML = '';
        Navigation.nav.innerHTML = '';

        let btn = document.createElement("button");
        btn.innerText = 'Submit';
        btn.setAttribute("id","btnSubmit");
        btn.setAttribute("class","btn");

        if(snippet === "example"){
            VideoInstructions.showInstructions(true);

        } else {

            //start test
           MessagePrompt.showMessage('Follow the ball which from the center of the screen\n will start to move in different directions.\n Try not to move your head and keep your eyes on the ball throughout the experiment.\n Click on START to start the experiment ',
                'START',function () {
                    this.intTmeOt = setTimeout(function () {
                     //   GazeDataCollection.restartEyeData();
                        renderBall.renderGreenBall(ballSpeed);
                        this.tO = setTimeout(function () {CodeDisplay.timeOut();}, exTime * 1000);
                    }, 200);
                });

        }
    },

    timeOut: function () {
        GazeDataCollection.pauseEyeData();
        MessagePrompt.showMessage("Experiment finished","Continue", function(){
            //short calibration
            EndOfExperiment.hasEnded("correct", "done")});
        },
};


/**
 Initiates the calibration process for the webgazer eye tracking software.
 Disables the Kalman filter, gets div dimensions, and sets calibration positions.
 Clears previous data to start calibration from scratch if not already calibrated.
 Shuffles and adds validation points last. Clears content and nav divs before starting calibration.
 Displays an image message prompting the user to look at a blue dot until it turns green.
 Calls the newCalibration function from GazeDataCollection and displays the calibration point after a 2-second timeout.
 */

let Calibration = {
    failedCalibrations: 0,
    isCalibrated:false,
    isCalibrating: false,
    sampleCount: 10,
    samplesLeft: undefined,
    calibrationPositions: undefined,
    areaHeight: undefined, areaWidth: undefined, areaX: undefined, areaY: undefined,

    dot: undefined,
    curPos: undefined,
    intTmeOt: undefined,



    calibrate: function(){
        //do not use filter when calibrating
        webgazer.applyKalmanFilter(false);

        CodeDisplay.snippetsShown = 0;
        this.curPoint = 0;
        this.isCalibrating = true;

        //get div dimensions
        let dotSize = 30;
        this.areaHeight = Navigation.content.offsetHeight - dotSize * 3;
        this.areaWidth = Navigation.content.offsetWidth - dotSize * 3;
        let rect = Navigation.content.getBoundingClientRect();
        this.areaX = rect.left;
        this.areaY = rect.top;

        this.calibrationPositions = [];

        // Number of calibration points - 20 (7 calibration points, 21 validation points) - optimal
        for(let r = dotSize; r <= dotSize + this.areaHeight; r = r + this.areaHeight / 3){
            for(let c = dotSize; c <= dotSize  + this.areaWidth; c = c + this.areaWidth / 3){
                this.calibrationPositions.push([c,r]);
            }
        }

        if(!this.isCalibrated){
            //clear previous data to start calibration from scratch
            webgazer.clearData();
            window.localStorage.clear();

            //Do full calibration
            // Number of calibration points - 20 (7 calibration points, 21 validation points) - optimal
            for(let r = dotSize + this.areaHeight / 6; r <= dotSize + this.areaHeight; r = r + this.areaHeight / 3){
                for(let c = dotSize + this.areaWidth / 6; c <= dotSize + this.areaWidth; c = c + this.areaWidth / 3){
                    this.calibrationPositions.push([c,r]);
                }
            }
        }

        //validation points (H formation covering center screen)
        let valPoints = [[this.areaWidth / 6 + dotSize, dotSize],
            [this.areaWidth / 6 * 5 + dotSize, dotSize],
            [this.areaWidth / 6 + dotSize, this.areaHeight / 6 * 3 + dotSize],
            [this.areaWidth / 6 * 5 + dotSize, this.areaHeight / 6 * 3 + dotSize],
            [this.areaWidth / 6 + dotSize, this.areaHeight + dotSize],
            [this.areaWidth / 6 * 5 + dotSize, this.areaHeight + dotSize],
            [this.areaWidth / 2 + dotSize, this.areaHeight / 2 + dotSize]];

        //shuffle points
        Util.shuffle(this.calibrationPositions);
        Util.shuffle(valPoints);

        //add validation points last
        Array.prototype.push.apply(this.calibrationPositions, valPoints);

        //Save total number of point for the calibration
        this.numPoints = this.calibrationPositions.length;

        //clear content div and nav div
        Navigation.content.innerHTML = '';
        Navigation.nav.innerHTML = '';

        //start calibrating
        MessagePrompt.showImgMessage('Please look at the blue dot <div class="calibDot" style="position: relative; display: inline-block"></div> ' +
            'until it turns green.',
            'pictures/eyesLook.png','Start calibration',function () {
                this.intTmeOt = setTimeout(function () {
                    GazeDataCollection.newCalibration();
                    Calibration.displayCalibrationPoint();
                }, 2000);
            });
    },

    timeout: function(){
        GazeDataCollection.pauseEyeData();
        MessagePrompt.showMessage("Calibration timed out","Restart calibration", function () {
            Calibration.calibrate();
        });
    },

    displayCalibrationPoint: function(){
        if(this.calibrationPositions.length === 7*2){
                //Validation has started
            this.isCalibrating = false;
        }

        this.samplesLeft = this.sampleCount;
        this.curPos = this.calibrationPositions.shift();
        this.dot = document.createElement("btn");
        this.dot.innerText = this.samplesLeft;
        this.dot.setAttribute("class", "calibDot unselectable");
        this.dot.style.left = Util.roundToTwo(this.curPos[0]) + 'px';
        this.dot.style.top = Util.roundToTwo(this.curPos[1]) + 'px';

        Navigation.content.appendChild(this.dot);
        this.intTmeOt = setTimeout(function () {
            //reset gaze data for each point
            GazeDataCollection.restartEyeData();
            Calibration.autoCalibrate();
        }, 1000);
    },

    autoCalibrate: function(){
        this.intTmeOt = setTimeout(function(){Calibration.calibrationHit();}, 100);
    },

    calibrationHit: function(){
        //Do until all samples taken
        if(this.samplesLeft > 0){
            this.samplesLeft--;

            if(this.isCalibrating){
                //Manually feeds training data to regression model
                webgazer.watchListener(this.curPos[0] + 10, this.curPos[1] + 10);
            }

            let blue = 255 / this.sampleCount * this.samplesLeft;
            let green =  255 / this.sampleCount * (this.sampleCount - this.samplesLeft);
            let red = this.sampleCount - this.samplesLeft;
            this.dot.style.background = 'rgb(' + red + ',' + green + ',' + blue + ')';
            this.dot.innerText = this.samplesLeft + 1;

            Calibration.autoCalibrate();
            return;
        }
        //pause webgazer to allow the stack to catch up???
        GazeDataCollection.pauseEyeData();

        //increment counter
        this.curPoint++;

        //save validation data
        if(!this.isCalibrating){
            GazeDataCollection.validationRead();
        }

        //clear div
        Navigation.content.innerHTML = '';

        //display new point
        if(this.numPoints > this.curPoint){
            setTimeout(function () {
                Calibration.displayCalibrationPoint();
            }, 500);
        }else{
            clearTimeout(this.intTmeOt);

            //send the data
            ServerCommunication.sendCalibrationData();
        }
    }
};


/**
 A method to render a green ball on a canvas with optional random movements and time interval.
 @param {number} desiredIntervalTime - Desired interval time in seconds (15, 30, or 60).
 @param {boolean} randomStart - Whether the ball should start with a random direction or not.
 @param {boolean} randomMoves - Whether the ball should have random movements or not.
 @param {string} backgroundImg - The file name of the background image.
 @param {number} ballSpeed - The speed of the ball in pixels per second.
 @param {number} exTime - The time in seconds until the ball stops moving.
 @param {number} demo - Whether the function is called for demo purposes or not.
 */
renderBall = {
    renderGreenBall: function () {

        // Clear content div and nav div for demo
        if (demo === 1) {
            //clear content div and nav div
            Navigation.content.innerHTML = '';
            Navigation.nav.innerHTML = '';
        }

        let intervalTime = 33;
        // Check if the desired interval time is valid (15, 30, or 60)
        if (desiredIntervalTime === 15 || desiredIntervalTime === 30 || desiredIntervalTime === 60) {
            intervalTime = 1000 / desiredIntervalTime; // calculate the interval time based on the desired value
        } else {
            console.log("Invalid desired interval time value. Using default value of 33ms.");
        }

        // Get canvas and set its dimensions
        const container = document.getElementById("contentDiv");
        const canvas = document.createElement("canvas");
        canvas.width = container.offsetWidth;
        canvas.height = container.offsetHeight;
        container.appendChild(canvas);

        // Get canvas context and background image
        const ctx = canvas.getContext("2d");
        const backgroundImage = new Image();
        backgroundImage.src = "backgroundIMG/" + backgroundImg;

        // Draw background image and set ball parameters
        backgroundImage.onload = function() {
            ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);
        };
        const radius = 25;
        let x = canvas.width / 2;
        let y = canvas.height / 2;
        let dx = 0;
        let dy = 0;

        // Set ball direction based on randomStart value
        if (randomStart) {
            dx = (Math.random() * 2 - 1) * ballSpeed;
            dy = (Math.random() * 2 - 1) * ballSpeed;
        } else {
            dx = ballSpeed;
            dy = -ballSpeed - 4;
        }
        let readings = [];
        let startTime = null; // initialize startTime to null

        /**
         A function to draw the green ball on the canvas.
         */
        function drawBall() {
            // Set start time and collect gaze data on the first call
            if (startTime === null) {
                startTime = performance.now();
                GazeDataCollection.restartEyeData();

            }
            // const currentTimestamp = performance.now() - startTime; // calculate the current timestamp by subtracting the initial timestamp from the current timestamp
            readings.push({
                timeStamp: performance.now(), // use the current timestamp
                x: Math.round(x),
                y: Math.round(y)
            });

            // Clear canvas, draw background image, and draw ball
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);

            // Create a gradient for the ball
            const gradient = ctx.createRadialGradient(
                x,
                y,
                radius * 0.4,
                x,
                y,
                radius
            );
            gradient.addColorStop(0, "green");
            gradient.addColorStop(1, "yellow");

            // Draw the ball
            ctx.beginPath();
            ctx.arc(x, y, radius, 0, Math.PI * 2);
            ctx.fillStyle = gradient;
            ctx.fill();
            ctx.closePath();

            // Check if the ball hits the edges of the canvas
            if (x + dx > canvas.width - radius || x + dx < radius) {
                dx = -dx; // Reverse the horizontal velocity
            }
            if (y + dy > canvas.height - radius || y + dy < radius) {
                dy = -dy; // Reverse the vertical velocity
            }

            // Add random movement if randomMoves is enabled
            if (randomMoves) {
                // Add random movement
                const randomMove = Math.random() * 2 - 1; // Returns a random number between -1 and 1
                dx += randomMove;
                dy += randomMove;

                // Limit the speed of the ball to ballSpeed
                const speedLimit = ballSpeed;
                const speed = Math.sqrt(dx * dx + dy * dy); // Calculate the speed of the ball
                if (speed > speedLimit) { // If the speed is greater than the speed limit
                    dx *= speedLimit / speed; // Scale down the horizontal velocity
                    dy *= speedLimit / speed; // Scale down the vertical velocity
                }
            }

            // Update the position of the ball
            x += dx;
            y += dy;

            // Check if the elapsed time is greater than exTime milliseconds
            if (performance.now() - startTime > exTime * 1000) {
                GazeDataCollection.pauseEyeData(); // Pause the eye data collection
                clearInterval(intervalId); // Clear the interval for drawing the ball
                const jsonData = JSON.stringify({ readings: readings }); // Convert the gaze data to JSON format
                if (demo !== 1) {
                    ServerCommunication.sendEyeOnDotPositionData();// Send the gaze data to the server
                    ServerCommunication.sendBallCoordinateData(jsonData); // Send the ball position data to the server
                }

                if (demo === 1) {
                    Navigation.content.innerHTML = ''; // Clear the content of the navigation panel
                    location.reload(true); // Reload the current page from the server
                    demo = 0; // Set demo to 0
                }
            }
        }
        // Set the interval for drawing the ball
        const intervalId = setInterval(drawBall, intervalTime);
    },
};


/**
 * The EndOfExperiment class is responsible for handling the end of the experiment and displaying the final content to the user.
 */
EndOfExperiment = {
    hasEnded: function (correct, total) {
        //show gaze point
        //     webgazer.showPredictionPoints(true).applyKalmanFilter(true);
        //     GazeDataCollection.resumeEyeData();

        //end of test, clear cookie
        document.cookie = "subjectID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

        //clear navigation div
        Navigation.nav.innerHTML = '';
        Navigation.content.innerHTML =
            '<div class="eye_container">\n' +
            '<header>\n' +
            '        <table>\n' +
            '            <thead>\n' +
            '        <h1 class="start-text">Benchmarking Extension</h1> \n' +
            '        <p class="topic-text">At the end of the experiment :)<br> \n' +
            '         </p> \n' +
            '            </thead>\n' +
            '        </table>\n' +
            '    </header>' +
            '<div id="txtDiv">\n' +
            '<div class="textBench" id="photoDiv">\n' +
            '        <p style="margin:auto"">Photo</p>' +
            '        <div id="videoDest" style="position:relative"></div>' +
            '        <p id="photoInst">It would be beneficiary for the study to know how you are seated and your webcam quality. Would you allow a ' +
            'photo to be saved from the webcam? This is of course optional and will not be published anywhere!</p>' +
            '        <button id="photoYes" class="">Yes</button>' +
            '        <button id="photoNo" class="">No</button>' +
            '    </div>' +
            '</hr>' +
            '   <div class="textBench" id="comment">' +
            '    <p>Thank you very much for your participation.</p>\n' +
            '      <p>Thank you for taking the time to do this experiment. The data generated will be invaluable as I write my thesis.</p>' +
            '       <p><label>Optional notes:<br/>' +
            '           <textarea id = "comments" rows = "4" cols = "36" placeholder="Any thoughts/feedback about the experiment?"></textarea>' +
            '       </label></p>' +
            '       <button type="submit" form="demogForm" id="subFeedBack" class="">SEND</button>' +
            '   </div>' +
            '</hr>' +
            '       <p class="endfoto"> <br>\n' +
            '       Thank you, we are grateful! <br>\n' +
            '       Sylwia and John</p>\n' +
            '   </div>' +
            '</div>';


        //connect listeners
        document.getElementById("photoNo").addEventListener("click",function () {
            document.getElementById("photoYes").style.display = "none";
            document.getElementById("photoNo").style.display = "none";
            document.getElementById("photoInst").innerHTML = document.getElementById("photoInst").innerHTML + "</br><strong>No photo</strong>";
        },false);
        document.getElementById("photoYes").addEventListener("click",function () {
            EndOfExperiment.takePhoto();
            document.getElementById("photoYes").style.display = "none";
            document.getElementById("photoNo").style.display = "none";
            document.getElementById("photoInst").innerHTML = document.getElementById("photoInst").innerHTML + "</br><strong>Photo sent!</strong>";
        },false);
        document.getElementById("subFeedBack").addEventListener("click",function () {
            document.getElementById("subFeedBack").disabled = true;
            document.getElementById("comments").disabled = true;
            UserData.feedback = document.getElementById("comments").value;
            ServerCommunication.sendFeedBack();
        },false);

        //get video
        let video = document.getElementById("webgazerVideoFeed");
        video.style.display = "block"; video.style.position = "relative";
        video.style.height = "200px"; video.style.width = "auto";

        //reposition video
        let vidDest = document.getElementById("videoDest");
        vidDest.innerHTML = '';
        vidDest.appendChild(video);

        //add comment to nav
        let willStop = document.createElement("p");
        willStop.innerText = "Access to your webcam will stop once this tab is closed.";
        Navigation.nav.appendChild(willStop);
    },

    takePhoto: function () {
        let canvas = document.getElementById('webgazerVideoCanvas');
        let data_out = canvas.toDataURL('image/jpeg');

        ServerCommunication.sendPhoto(data_out);
    }
};

window.addEventListener("load", function () {
    MessagePrompt.init();
    Navigation.init();
},false);
