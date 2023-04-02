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
    <title>Benchmarking Estension</title>
    <link rel="stylesheet" href="CSS/index.css">
</head>

<body>
<!-- <img src="pictures/uniLogo.png" class="uniLogo"> -->

            <div class="index_container">
                <h1 class="start-text">WELCOME!</h1>

                <table id="eyeTable">
                    <Div class="text">
                        <p></p>
                        We are two students excited to share our latest project with you. <br>
                        Our goal was to develop a benchmarking tool to assess the accuracy <br>
                        of <b>Eye-Tracking</b> software and extend the existing program  <br>
                        <b>"EyesOnTheCode"</b> with a benchmarking feature. <br>
                        Any changes will be limited to this improvement.
                        </p>
                        <hr>
                        <p>
                        Our tool is an excellent addition to the existing program and provides <br>
                        a valuable service to Eye-Tracking software users. <br>
                        With our benchmarking feature, users can assess the accuracy <br>
                            of their <b>Eye-Tracking software,</b><br>
                        using a new dynamic method.
                        </p>
                        <hr>
                        <p>
                            Below on the page, you can find two buttons:<br> <b>"Eye Tracking Study"</b> and <b>"Benchmarking Extension"</b>.<br>
                            The <b>"Eye Tracking Study"</b> is a project created by the previous student, <br> <b>Eva Thilderkvist,</b>
                            while the <b>"Benchmarking Extension"</b> is our tool.
                        </p>
                        <hr>
                        <p>
                        We appreciate your interest and hope you find our tool helpful.<br>
                        Let us know if you have any questions or suggestions.
                        </p>
                        <hr>
                        <p class="thanksfoto">
                        Thank you for your time! <br>
                            Sylwia and John
                        </p>
                    </Div>
                </table>

                <div class="button-container">
                    <a class="button" onclick="document.location='../../ReadGazeStudy/index.php'">Eye Tracking Study</a>
                    <a class="button" onclick="document.location='indexBenchmarking.php'">Benchmarking Extension</a>
                </div>

            </div>


<div id="hidden">
    <p id="origin">
        <?php echo $origin ?></p>
</div>
</body>

</html>
