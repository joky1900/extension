<?php
/**
 * Date: 2021-04-22
 * Time: 11:40
 */

// Get the origin variable if it is set
$origin = '';
if (isset($_GET['g'])) {
    $origin = $_GET['g'];
}

// Use the $origin variable as needed
?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Benchmarking Extension</title>
    <script src="js/webgazer.js" type="text/javascript" async></script>
    <script src="js/navigation.js" type="text/javascript"></script>
    <script src="js/gazedatacollection.js" type="text/javascript"></script>
    <script src="js/datacommunication.js" type="text/javascript"></script>
    <script src="js/util.js" type="text/javascript"></script>
    <link rel="stylesheet" href="CSS/index.css">
</head>

<body>


<div id="contentDiv" >
    <div class="eye_container">

        <header>
            <table>
                <thead>
                <h1 class="start-text">Benchmarking Extension</h1>
                <p class="topic-text">Evaluating the Accuracy of Eye-Tracking Software:<br>
                    Analysis of Eye Tracking Technologies in Dynamic Situations.</p>
                </thead>
            </table>
        </header>



        <div id="txtDiv">
            <table id="eyeTable">
                <div class="textBench">
                    <p>
                    In our bachelor's thesis, we decided to develop a benchmarking tool
                    designed to assess the accuracy of the <b>Eye-Tracking software </b>. It is an extension of the existing
                    <b>"EyesOnTheCode" </b>program with benchmarking functionality.
                    </p>
                        <hr>
                    <p>
                    This extension introduces a dot that changes its position on the screen during the test. <br>
                    The dot will supplement the lack of tracking during dynamic actions. Additionally, it will check the
                    efficiency and, most importantly, the accuracy of the algorithm when the dot is being tracked. <br>
                    The tool will evaluate the software's performance by displaying a moving dot on the screen,
                    and at the same time, recording the eye movement of the user. <br>
                    It will complement the already implemented method of collecting results during eye tracking.
                    </p>
                        <hr>
                    <p>
                    <b> Data to be collected: </b> <br>
                    * Eye coordinates (x,y) will be collected using an eye tracking algorithm.
                    </p>
                        <hr>
                    <p>
                    <b> Conditions: </b> <br>
                    *   The webpage will require access to your web camera to collect data. <br>
                    *   You need to be seated in front of a computer screen and not a mobile device. <br>
                    *   A few non-invasive questions will be asked about you and your programming experience to help establish subject demographics. <br>
                    *   The entire experiment typically takes around 15 minutes to complete. <br>
                    *   <b>No video recordings will be collected or saved. </b> <br>
                    </p>
                        <hr>
                    <p class="okfoto">
                    We appreciate your support! <br>
                    Sylwia and John
                    </p>

                </div>
            </table>

        </div>
    </div>
</div>

        <div id="navDiv">
            <img src="pictures/uniLogo.png" alt="Mid Sweden University" style="position: center; id="uniLogo">
            <div  id="divButtons">
                <button  id="btnPartake">I will participate</button>
            </div>
        </div>


<div id="overlay">
    <div id="prompt">
        <div id="loader"></div>
        <img src="" id="promptImg" alt="promtImage" hidden>
        <p id="promptTxt"></p>
        <button id="okBtn" style="display:none;"></button>
    </div>
</div>




<div id="hidden">
    <p id="origin">
        <?php /*echo $origin */?></p>
</div>
</body>

</html>
