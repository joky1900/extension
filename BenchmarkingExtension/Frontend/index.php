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
    <title>Eye-tracking-study</title>
    <script src="js/webgazer.js" type="text/javascript" async></script>
    <script src="js/navigation.js" type="text/javascript"></script>
    <script src="js/gazedatacollection.js" type="text/javascript"></script>
    <script src="js/datacommunication.js" type="text/javascript"></script>
    <script src="js/util.js" type="text/javascript"></script>
    <link rel="stylesheet" href="CSS/index.css">
</head>

<body>
<div id="contentDiv">


    <div class="wrapper">

        <div class="content">



            <div class="eye_container">
                <h1 class="start-text">WELCOME!</h1>

                <table id="eyeTable">
                    <tr>
                        <th></th>
                        <th id="matched">Name</th>
                        <th>Email</th>
                        <th>Contact Information</th>
                    </tr>
                </table>

            </div>
        </div>




    </div>

    <div class="footer">

        <p class="button">
            <button onclick="document.location='indexStatic.php'">Static test</button>
            <button onclick="document.location='indexStatic.php'">Dynamic test</button>
        </p>
    </div>

</div>


<div id="overlay">
    <div id="prompt">
        <div id="loader"></div>

        <p id="promptTxt"></p>

    </div>
</div>

</div>
<div id="hidden">
    <p id="origin">
        <?php echo $origin ?></p>
</div>
</body>
</html>
