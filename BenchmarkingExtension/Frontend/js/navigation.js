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

    showExpInstructions: function () {
        //change button
        this.nav.innerHTML = '<button id="btnStart"  class="divButtons" style="display: margin: auto;">Start</button>';
        document.getElementById("btnStart").addEventListener("click", function () {Questionnaire.showQuestionnaire(); }, false);

        //change text strona trzecia trzeba zmienic tekst
        this.txtDiv.innerHTML =
            '<style>.textBench {margin: 50px 50px; padding: 10px; text-align: left; color: navy; font-family: "Times New Roman", sans-serif; font-size: 17px; text-shadow: darkseagreen 1px 0 5px; line-height: 1.5em;}</style>' +
            '<div class="textBench"><h4>How it works</h4>\n' +
            '<p>Several screens with text or code will be presented during the test. ' +
            'A dot will appear on the text on which the user should focus their eyes, then start reading the text from the point marked by the dot. ' +
            'The experiment will be repeated at different distances from the camera, ' +
            'as well as the situation when the participant moves his head to the right and left will be tested. </p> <hr>'+

            '</p>It is very important that the camera is placed more or less at the level of the user\'s eyes.' +
            'It should also be positioned straight ahead.</p> <hr>' +

            '<p>The camera must be calibrated before the test. ' +
            'During it, the user should look at the small circles with numbers inside that appear on the screen. ' +
            'When the number reaches 0, a new circle will appear in a different place on the screen. Follow these points with your eyes until calibration is complete. ' +
            'This test is not long, try to keep your head still during this experiment. <hr>' +
            'The only exception will be a test that involves moving the head sideways, then you should direct your head towards the moving ' +
            'thick bar at the top of the screen, while reading the text from the place designated by the dot. ' +
            'Please follow the additional instructions on the screen. </p>'+

            '<img src="pictures/works.png" alt="Works Image" style="left: 300px; top: 0; noRepeat: true; padding-left: 10px;">';

        }

    };

/*========================================================================================================================
Questionnaire Benchmarking extension
 */
Questionnaire = {
    showQuestionnaire: function () {
        Navigation.nav.innerHTML ='<button type="submit" form="demogForm" id="btnSubmit" class="divButtons">Continue</button>';

        Navigation.txtDiv.innerHTML =
            '<h4 style="text-align: center; margin: 10px; padding: 10px; background-color: #f0f0f0; border: 0px solid #e96cab; border-radius: 5px; box-shadow: 0 0 5px rgb(229,1,1), 0 0 10px rgba(0, 0, 255, 0.3) inset;">PLEASE ENTER AS ACCURATE AS POSSIBLE:</h4>\n\n\n' +
            '<form id="demogForm" style="margin: 20px; padding: 20px; background-color: #fafafa;' +
            ' border: 1px solid #6cafe9; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 255, 0.5), 0 0 10px rgba(0, 0, 255, 0.3) inset;">\n' +
            '          <p>\n' +
            '              <strong>Gender: </strong>\n' +
            '              <label><input name="gender" type="radio" value="female" checked style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6);"> Female</label>\n' +
            '              <label><input name="gender" type="radio" value="male" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6);"> Male</label>\n' +
            '              <label><input name="gender" type="radio" value="other" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6);"> Other</label>' +
            '          </p>\n' +
            '          <p>\n' +
            '              <strong>Age: </strong>\n' +
            '              <label><input id="ageTxt" name="age" type="text" size="2" autocomplete="off" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6); border-radius: 5px; font-size: 15px;">\n</label>\n' +
            '          </p>\n' +
            '          <p>' +
            '              <strong>First language: </strong>' +
            '              <label><input id="fstLanguage" name = "fstLan" type = "text" autocomplete="off" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6); border-radius: 5px; font-size: 15px;"></label>\n' +
            '          </p> ' +
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
            '   <p>\n' +
            '       <h4 style="margin-bottom: 10px;"> Previous experience:</h4><br/>\n' +
            '       <label style="display: block; margin-bottom: 10px;">Java: <br/>\n' +
            '           <select name = "javaExp" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6); font-size: 15px; border-radius: 5px;">\n' +
            '               <option selected>None</option>\n' +
            '               <option>Hobby level with moderate understanding</option>\n' +
            '               <option>Hobby level with good understanding</option>\n' +
            '               <option>Very capable but no official schooling or work experience</option>\n' +
            '               <option>Undergraduate education</option>\n' +
            '               <option>Postgraduate education</option>\n' +
            '               <option>Professional programmer</option>\n' +
            '           </select>\n' +
            '       </label>' +
            '   </p>' +
            '   <p>Are you familiar with the reactive programming paradigm?</p>' +
            '   <p>' +
            '       <label>Reactive programming experience: <br/>\n' +
            '           <select name = "reactExp" style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6); font-size: 15px; border-radius: 5px;">\n' +
            '               <option selected>None</option>\n' +
            '               <option>Hobby level with moderate understanding</option>\n' +
            '               <option>Hobby level with good understanding</option>\n' +
            '               <option>Very capable but no official schooling or work experience</option>\n' +
            '               <option>Undergraduate education</option>\n' +
            '               <option>Postgraduate education</option>\n' +
            '               <option>Professional programmer</option>\n' +
            '           </select>\n' +
            '       </label>\n' +
            '   </p>\n' +
            '<p><label>Optional notes:<br/><textarea name = "comments" rows = "4" cols = "36" placeholder="ex. web camera specifications' +
            ', educational/work background..." style="box-shadow: inset 0 2px 3px rgba(0,0,0,0.1), 0 0 10px rgba(102,175,233,0.6); font-size: 15px; border-radius: 5px;"></textarea></label></p>' +
            '</form>';

        //Disable button and force age validation
        document.getElementById("ageTxt").addEventListener("input", function () { Questionnaire.validateAge(); }, false);

        //validate in case of page reload
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

VideoInstructions = {
    setUpTO: undefined,

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

CodeDisplay = {
    snippetsShown: 0,
    dims: undefined,
    tO: undefined,
    allShown: false,
    codeHelp: "Read the code and enter the output values produced by the <code class=\"code\">System.<span class=\"blue\">out</span>.print()</code> statement. " +
    "In case of multiple <code class=\"code\">System.<span class=\"blue\">out</span>.print()</code> statements enter your answers sequentially. If you prefer, you can separate " +
    "them with a space.",
    storyHelp: "Read the story extract and enter the answer to the question in the textbox." ,

  display: function (snippet) {
       /* //check if this is the example, then show the instructions
        if(snippet === "example"){
            //Practice prompt
            MessagePrompt.showMessage('You will now be shown a practise code snippet.<br/>'
                + 'Read the code and enter all the output values produced by the <code class="code">System.<span class="blue">out</span>.print()</code> statement. ' +
                'In case of multiple <code class="code">System.<span class="blue">out</span>.print()</code> '
                + 'statements, you can enter your answers separated by a space if you prefer.'
                , 'Start practise', function () {
                    document.getElementById("answerInput").focus();
                });
        }

        //display snippet image
        let codeImg = document.createElement("img");
        codeImg.setAttribute("src", "codeSnippets/" + snippet + ".png");
        codeImg.setAttribute("id", "snippet");*/
        Navigation.content.innerHTML = '';
        //Navigation.content.appendChild(codeImg);

        Navigation.nav.innerHTML = '';

        let help = document.createElement("button");
        help.innerText = '?';
        help.setAttribute("id", "helpBtn");
        help.addEventListener("click", function () {
            GazeDataCollection.pauseEyeData();
            clearTimeout(CodeDisplay.tO);

            let help = CodeDisplay.codeHelp;
            if(snippet.replace(/[0-9]/g, '') === "S"){
                help = CodeDisplay.storyHelp;
            }

            MessagePrompt.showMessage(help, "Continue", function () {
                //resetTimeout
                if(snippet !== "example") {
                    CodeDisplay.tO = setTimeout(function () {
                        CodeDisplay.timeOut();
                    }, 120000);
                }
                GazeDataCollection.resumeEyeData();
            })
        },false);

       /* let headImg = document.createElement("img");
        headImg.setAttribute("src", "pictures/eyesLook.png");
        headImg.setAttribute("id", "eyesLook");
        Navigation.nav.appendChild(headImg);

        let inp = document.createElement("input");
        inp.setAttribute("id", "answerInput");
        inp.setAttribute("autocomplete", "off");
        inp.style.margin = "auto";
        inp.name = "answer";
        inp.type = "text";
        inp.style.width = "90%";

        let btn = document.createElement("button");
        btn.innerText = 'Submit';
        btn.setAttribute("id","btnSubmit");
        btn.setAttribute("class","button");

        Navigation.nav.appendChild(inp);
        Navigation.nav.appendChild(btn);
        Navigation.nav.appendChild(help);

        inp.focus();*/

        if(snippet === "example"){
            //this is the example snippet
           // btn.addEventListener("click", function () { CodeDisplay.exampleAnswer(); }, false);

            console.log("kalibracja");
           // VideoInstructions.showInstructions(true);

            MessagePrompt.showMessage("Calibration finished","Ok", function(){

                //short calibration
                //Calibration.calibrate()
                renderBall.renderGreenBall()});
          



        }else{

            //this is a true test
            btn.addEventListener("click", function () { CodeDisplay.answer(snippet); }, false);

            GazeDataCollection.restartEyeData();
            //Give maximum 6 minutes for each snippet
            this.tO = setTimeout(function () {CodeDisplay.timeOut();}, 120000);
        }
    },

    timeOut: function () {
        GazeDataCollection.pauseEyeData();
        MessagePrompt.showMessage("Snippet timed out","Resume", function(){
            //short calibration
            Calibration.calibrate()});
    },

    exampleAnswer: function () {
        MessagePrompt.showMessage('Correct!', 'Start experiment', function(){VideoInstructions.showInstructions(true);});

        /*        let answer = document.getElementById("answerInput").value.toString().replace(/\s/g,'').toUpperCase();
                if(answer === ''){
                    //do nothing, need an answer
                }else if(answer === '515' || answer === '5 15'){
                    MessagePrompt.showMessage('Correct!', 'Start experiment', function(){VideoInstructions.showInstructions(true);});
                }else{
                    MessagePrompt.showMessage('Wrong answer, correct was:<br/>5 15 (or 515)', 'Start experiment',
                        function(){VideoInstructions.showInstructions(true);});
                }*/
    },

    answer: function (snippet) {
        //answer
        let inp = document.getElementById("answerInput");
        let answer = inp.value;

        //check if answer entered
        if(answer === ''){
            return;     //do nothing
        }

        //disable input
        inp.disabled = true;
        document.getElementById("btnSubmit").disabled = true;

        clearTimeout(this.tO);
        this.snippetsShown++;

        ServerCommunication.sendEyeData(snippet, answer);
    },
};

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
        let dotSize = 20;
        this.areaHeight = Navigation.content.offsetHeight - dotSize * 3;
        this.areaWidth = Navigation.content.offsetWidth - dotSize * 3;
        let rect = Navigation.content.getBoundingClientRect();
        this.areaX = rect.left;
        this.areaY = rect.top;

        this.calibrationPositions = [];
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
        if(this.calibrationPositions.length === 7){
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

renderBall = {
    renderGreenBall: function () {
        // Get the HTML element where the ball will be rendered
        const container = document.getElementById("contentDiv");

        // Create a canvas element and add it to the container
        const canvas = document.createElement("canvas");
        canvas.width = container.offsetWidth;
        canvas.height = container.offsetHeight;
        container.appendChild(canvas);

        // Set the drawing context to 2D
        const ctx = canvas.getContext("2d");

        // Load the background image and draw it on the canvas
        const backgroundImage = new Image();
        backgroundImage.src = "pictures/ballBackground.jpg";
        backgroundImage.onload = function() {
            ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);
        };

        // Set the initial position and radius of the ball
        let x = canvas.width / 2;
        let y = canvas.height / 2;
        const radius = 20;

        // Set the speed and direction of the ball
        let dx = 0.5;
        let dy = -0.5;

        // Create a function to render the ball in a new position
        function drawBall() {
            // Clear the canvas
            ctx.clearRect(0, 0, canvas.width, canvas.height);

            // Draw the background image
            ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);

            // Set a radial gradient on the ball
            const gradient = ctx.createRadialGradient(
                x,
                y,
                radius * 0.4,
                x,
                y,
                radius
            );
            gradient.addColorStop(0, "green");
            gradient.addColorStop(1, "black");

            // Draw the ball with the gradient
            ctx.beginPath();
            ctx.arc(x, y, radius, 0, Math.PI * 2);
            ctx.fillStyle = gradient;
            ctx.fill();
            ctx.closePath();

            // Check if the ball hits the walls of the canvas
            if (x + dx > canvas.width - radius || x + dx < radius) {
                dx = -dx;
            }
            if (y + dy > canvas.height - radius || y + dy < radius) {
                dy = -dy;
            }

            // Add random values to the ball's speed
            if (Math.random() > 0.9) {
                dx += Math.random() - 0.5;
                dy += Math.random() - 0.5;
            }

            // Limit the ball's speed values to the range [-1, 1]
            dx = Math.min(1, Math.max(-1, dx));
            dy = Math.min(1, Math.max(-1, dy));

            // Move the ball by its speed in each animation cycle
            x += dx;
            y += dy;
        }
        setTimeout(function () {
            GazeDataCollection.pauseEyeData();
            container.removeChild(canvas);
            EndOfExperiment.hasEnded("correct, total");

        }, 9000);

        // Run the rendering function with a frequency of 10ms
        setInterval(drawBall, 10);

    },


};




EndOfExperiment = {
    hasEnded: function (correct, total) {
        //show gaze point
        webgazer.showPredictionPoints(true).applyKalmanFilter(true);
        GazeDataCollection.resumeEyeData();

        //end of test, clear cookie
        document.cookie = "subjectID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

        //clear navigation div
        Navigation.nav.innerHTML = '';

        Navigation.content.innerHTML =
            '<header>\n' +
            '        <table>\n' +
            '            <thead>\n' +
            '            <tr>\n' +
            '                <th class="hImg">\n' +
            '                    <img src="pictures/uniLogo.png" alt="Mid Sweden University" id="leftImg">\n' +
            '                </th>\n' +
            '                <th class="hTxt" >\n' +
            '                    <h2>Eye tracking study</h2>\n' +
            '                    <h3>Visual processing of computer code using webcam gaze prediction</h3>\n' +
            '                </th>\n' +
            '               </tr>\n' +
            '            </thead>\n' +
            '        </table>\n' +
            '    </header>' +
            '<div id="txtDiv">\n' +
            '   <div class="row">\n' +
            '      <div class="column" id="photoDiv">' +
            '        <h4 style="margin:auto"">Photo</h4>' +
            '        <div id="videoDest" style="position:relative"></div>' +
            '        <p id="photoInst">It would be beneficiary for the study to know how you are seated and your webcam quality. Would you allow a ' +
            'photo to be saved from the webcam? This is of course optional and will not be published anywhere!</p>' +
            '        <button id="photoYes">Yes</button>' +
            '        <button id="photoNo">No</button>' +
            '    </div>' +
            '   <div id="comment" class="column">' +
            '    <h4>Thank you very much for your participation.</h4>\n' +
            '    <h5>You got ' + correct + ' of ' + total + ' snippets correct.</h5>' +
            '      <p>Thank you for taking the time to do this experiment. The data generated will be invaluable as I write my thesis.</p>' +
            '       <p><label>Optional notes:<br/>' +
            '           <textarea id = "comments" rows = "4" cols = "36" placeholder="Any thoughts/feedback about the experiment?"></textarea>' +
            '       </label></p>' +
            '       <button id="subFeedBack">Send</button>' +
            '       <p>//Eva </br>evth1400@student.miun.se</p>' +
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
