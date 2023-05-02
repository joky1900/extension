/**
 Collection of gaze data and methods to collect and manipulate it.
*/
GazeDataCollection = {
    gazeData: [],
    validationReads: [],
    isRunning: false,

    startCollectingCameraData: async function(){
        MessagePrompt.showLoader();
        webgazer.params.showVideoPreview = true;

        await webgazer.setRegression('threadedRidge')
            .setTracker('TFFacemesh')
            .showVideoPreview(true) /* shows all video previews */
            .showPredictionPoints(false) /* shows a square every 100 milliseconds where current prediction is */
            .applyKalmanFilter(false) /* Kalman Filter defaults to on. Raw data will be collected instead */
            .setGazeListener(this.gazeListener)
            .begin();

        await VideoInstructions.relocateVideo();

        this.isRunning = true;
    },

    gazeListener: function (data, clock) {
        if (data === null) {
            return;
        }

        let gaze = {};
        gaze["timeStamp"] = performance.now();
        console.log("clock: " + clock);
        //Round the coordinates as too many fraction pixels
        gaze["x"] = Util.roundToTwo(data.x);
        gaze["y"] = Util.roundToTwo(data.y);

       GazeDataCollection.gazeData.push(gaze);

    },

    restartEyeData: function () {
        webgazer.resume();
        this.gazeData = [];
    },

    pauseEyeData: function () {
      webgazer.pause();
    },

    resumeEyeData: function () {
      webgazer.resume();
    },
    
    newCalibration: function () {
        this.restartEyeData();
        this.validationReads = [];
    },

    validationRead: function () {
        let validationData = {};

        validationData["gazeData"] = this.gazeData;

        //get mouse click coordinates
        validationData["xPos"] =  Util.roundToTwo(Calibration.curPos[0] + 10);
        validationData["yPos"] =  Util.roundToTwo(Calibration.curPos[1] + 10);

        validationData["pointNumber"] = Calibration.curPoint;

        this.validationReads.push(validationData);
    }
};

//Do not save calibration data, always recalibrate
window.saveDataAcrossSessions = false;
