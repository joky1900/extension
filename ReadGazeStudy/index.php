<?php
/**
 * Date: 2021-04-22
 * Time: 11:40
 */

//Get the origin variable
$origin = $_GET['g'];
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Eye-tracking-study</title>
    <script src="js/webgazer.js" type="text/javascript" async></script>
    <script src="js/navigation.js" type="text/javascript"></script>
    <script src="js/gazedatacollection.js" type="text/javascript"></script>
    <script src="js/datacommunication.js" type="text/javascript"></script>
    <script src="js/util.js" type="text/javascript"></script>
    <link rel="stylesheet" href="style/eyestyle.css">
</head>
<body>
<div id="contentDiv">
    <header>
        <table>
            <thead>
            <tr>
                <th class="hImg">
                    <img src="img/uniLogo.png" alt="Mid Sweden University" id="leftImg">
                </th>
                <th class="hTxt">
                    <h2>Eye tracking study</h2>
                    <h3>Visual processing of computer code using webcam gaze prediction</h3>
                </th>
                <th class="hImg">
                    <img src="img/programmer.bmp" alt="programmer" id="rightImg">
                </th>
            </tr>
            </thead>
        </table>
    </header>

    <div id="txtDiv">
        <h4>Welcome!</h4>
        <p> As a part of my bachelor thesis I am examining the eye-movements of programmers as they read different types of text/code.
            This is done by measuring eye-gaze coordinates generated with standard webcams. By locating the position of the iris in the video feed,
            an algorithm can map it to a predicted gaze point on the screen. The objective of the study is both to test the eye-tracking technology and
            to analyse the data to identify different reading patterns.
            You will be asked to read a number of <u>short easy</u> Java code snippets as well as short story extracts while a script processes your video feed.
            </br><b><u>You should be familiar with programming to partake</u></b>.
        </p>

        <h4>What data will be collected?</h4>
        <p>The experiment is built with a client side eye tracking algorithm. This means <b><u>no video will be collected or saved</u></b>. The webpage will
            need access to your web camera and will apply an algorithm before saving data in the form of (x, y) coordinates only. You will also be asked
            a few non invasive questions about you and your programming experience to help establish subject demographics.</p>
        <p>You need to be seated in front of a computer screen and not a mobile device!
            &nbsp;<br/>The experiment takes about 15 minutes to complete.</p>
        <p>I am very grateful for your help and participation.</p><p><strong>/Eva</strong></br> evth1400@student.miun.se</p>
    </div>
</div>

<div id="navDiv">
    <img src="img/uniLogo.png" alt="Mid Sweden University" id="uniLogo">
    <div id="divButtons">
        <button id="btnPartake" class="btn">I would like to partake</button>
    </div>
</div>
<div id="overlay">
    <div id="prompt">
        <div id="loader"></div>
        <img src="" id="promptImg" alt="prompt image">
        <p id="promptTxt"></p>
        <button id="okBtn" class="btn"></button>
    </div>
</div>
<div id="hidden">
    <p id="origin"> <?php echo $origin ?></p>
</div>
</body>
</html>



