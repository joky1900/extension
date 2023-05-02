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
                <h1 id="start-text" class="start-text">Benchmarking Extension</h1>
                <p id="topic-text" class="topic-text">Evaluating the Accuracy of Eye-Tracking Software:<br>
                    Analysis of Eye Tracking Technologies in Dynamic Situations.</p>
                </thead>
            </table>
        </header>



        <div id="txtDiv">
            <table id="eyeTable">
                <div class="textBench">
                    <ol type="I">
                        <li>
                            In our bachelor's thesis, we developed a benchmarking tool designed
                            to assess the accuracy of the <b>Eye-Tracking software. </b>
                            It is an extension of the existing <b>"EyesOnTheCode" </b> program
                            with benchmarking functionality.
                        </li>
                        <hr>

                        <li>
                            This extension introduces a <b>dot</b> that changes its position on the screen during the test.
                            The <b>dot</b> supplements the lack of tracking during dynamic actions.
                            It checks the efficiency and the accuracy of the algorithm when
                            the <b>dot</b> is being tracked.
                        </li>
                        <hr>

                        <li>
                            The tool evaluates the software's performance by displaying a moving dot on the screen,
                            and at the same time, recording the eye movement of the user.
                        </li>
                        <hr>

                        <li>
                            The process of summarizing the data is automated and produces tables and/or charts
                            that provide valuable insights into the impact of different conditions on the accuracy
                            of <b>Eye-Tracking</b> systems.
                        </li>
                        <hr>

                        <li>
                            The data gathered from our study shows how different factors affect
                            the reading of <b>EyesOnTheCode / Webgazer</b>
                            and exemplifies how the tool can be useful for researchers
                            who are dependent on accurate readings when conducting research.
                        </li>
                        <hr>

                        <li>
                            The source code is published on <b>GitHub</b>,
                            under the GNU General Public License, and is accessible for free.
                        </li>
                        <hr>
                        <p class="okfoto">
                            Good luck! <br>
                            Sylwia and John
                        </p>

                    </ol>

                </div>
            </table>
        </div>
    </div>
</div>

        <div id="navDiv">
            <img src="pictures/uniLogo.png" alt="Mid Sweden University" style="position: center; transform: scale(0.75); id="uniLogo">
            <div  id="divButtons">
                <button  id="btnPartake">I will participate</button>
            </div>
        </div>

<!--<img src="pictures/uniLogo.png" id="image" alt="Mid Sweden University" style="position: center; transform: scale(0.75); >-->


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
