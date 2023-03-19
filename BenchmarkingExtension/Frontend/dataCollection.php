<?php
/**
 * Created by PhpStorm.
 * User: evath
 * Date: 2021-04-23
 * Time: 10:30
 */

header('Content-Type: application/json');
date_default_timezone_set('Europe/Stockholm');

//$filePath = '/../writeable/eyeGaze/';

//for local development
$filePath = './writable/';

//
$totQuestions = 12;

//check what kind of data packet is sent
switch ($_GET['dataType']){
    case 'demographics':
        $actionType = '_calibration';
        //first communication with subject. Generate an unique id
        $subjectID = uniqid("subject");

        //get data
        $data = file_get_contents('php://input');
        $saveData["subjectDetails"] = json_decode($data);


        //set up empty arrays for calibration and answers
        $saveData["calibrations"] = array();
        $saveData["eyeOnBall"] = array();
        $saveData["ballCoordinates"] = array();

        //create a data file
       // file_put_contents(__DIR__.$filePath.$subjectID.".json", json_encode($saveData));
        file_put_contents($filePath.$subjectID.$actionType.".json", json_encode($saveData));
        //return the id to client
        echo json_encode($subjectID);

        break;
    case 'calibration':
        $actionType = '_calibration';

        //get data from post
        $data = json_decode(file_get_contents('php://input'));

        //get saved data
        $subjectID = $_GET['subID'];
        //$fileData = json_decode(file_get_contents(__DIR__.$filePath.$subjectID .'.json'), true);
        $fileData = json_decode(file_get_contents($filePath.$subjectID.$actionType .'.json'), true);


        $newData["readings"] = $data->calibData;
        $newData["dimensions"] = $data->dimensions;

        //save current time
        $newData['time'] = date('Y-m-d H:i:s');

        array_push($fileData["calibrations"], $newData);

        //save back to file
        //file_put_contents(__DIR__.$filePath . $subjectID .".json", json_encode($fileData));
        file_put_contents($filePath . $subjectID.$actionType .".json", json_encode($fileData));

        //calculate the precision and send back
        $eD = round (calculatePrecision($data->calibData), 2 , PHP_ROUND_HALF_UP ) ;

        echo json_encode($eD);

        break;
    case 'ballTracing':
        $actionType = '_ballTracing';

        //get data from post
        $data = json_decode(file_get_contents('php://input'));

        //get saved data
        $subjectID = $_GET['subID'];
        $fileData = json_decode(file_get_contents($filePath.$subjectID.$actionType .'.json'), true);

        //add the new data
        $newData["time"] = date('Y-m-d H:i:s');
        $newData["gazeData"] = $data->gazeData;
        $newData["dimensions"] = $data->dimensions;
        $fileData['eyeOnBall'][] = $newData;

        //save back to file
        file_put_contents($filePath.$subjectID.$actionType .'.json', json_encode($fileData));
        break;
    case 'ballCoordinate':
        $actionType = '_ballCoordinates';

        //get data from post
        $data = json_decode(file_get_contents('php://input'));

        error_log(print_r($data, true));

        //get saved data
        $subjectID = $_GET['subID'];
        $fileData = json_decode(file_get_contents($filePath.$subjectID.$actionType .'.json'), true);

        //add the new data
        $newData["time"] = date('Y-m-d H:i:s');
        $newData["ballPosition"] = $data->ballPosition;
        $newData["dimensions"] = $data->dimensions;
        $fileData['ballCoordinates'][] = $newData;
        //save back to file
        file_put_contents($filePath.$subjectID.$actionType .'.json', json_encode($fileData));
        break;
    case 'technical':
        $actionType = '_calibration';

        //get data from post
        $data = json_decode(file_get_contents('php://input'));

        //get saved data
        $subjectID = $_GET['subID'];
        //$fileData = json_decode(file_get_contents(__DIR__.$filePath.$subjectID .'.json'), true);
        $fileData = json_decode(file_get_contents($filePath.$subjectID.$actionType .'.json'), true);

        //Add new data
        $fileData["Technical"] = $data;

        //save back to file
        //file_put_contents(__DIR__.$filePath.$subjectID .'.json', json_encode($fileData));
        file_put_contents($filePath.$subjectID.$actionType .'.json', json_encode($fileData));

        echo json_encode("ok");     //change to return of correct answer?

        break;
    case 'feedback':
        $actionType = '_calibration';

        //get data from post
        $data = file_get_contents('php://input');

        //get saved data
        $subjectID = $_GET['subID'];
        //$fileData = json_decode(file_get_contents(__DIR__.$filePath.$subjectID .'.json'), true);
        $fileData = json_decode(file_get_contents($filePath.$subjectID.$actionType .'.json'), true);

        //Add new data
        $fileData["Feedback"] = $data;

        //save back to file
        //file_put_contents(__DIR__.$filePath.$subjectID .'.json', json_encode($fileData));
        file_put_contents($filePath.$subjectID.$actionType .'.json', json_encode($fileData));

        break;
    case 'photo':
        $actionType = '_photo';

        //get data from post
        $data_url = file_get_contents('php://input');

        //get subject id
        $subjectID = $_GET['subID'];

        list($type, $data) = explode(';', $data_url);
        list(, $data)      = explode(',', $data);
        $data = base64_decode($data);

        //save
        //file_put_contents(__DIR__.$filePath.$subjectID.'.jpg', $data);
        file_put_contents($filePath.$subjectID.$actionType.'.jpg', $data);
        break;
}

//request
switch ($_GET['request']){
    case 'nextSnippet':
        $actionType = '_calibration';

        $subjectID = $_GET['subID'];

        //$userData = json_decode(file_get_contents(__DIR__.$filePath.$subjectID .'.json', true));
        $userData = json_decode(file_get_contents($filePath.$subjectID.$actionType .'.json', true));
        $snipLeft = $userData -> snippetsLeft;

        $next["done"] = count($userData -> answers);
        if($next["done"] < $totQuestions && count($snipLeft) > 0){
            //check if a question has been missed
            $lastDone = end($userData -> snippetOrder);
            $hasMissed = true;
            for($i = 0; $i < count($userData -> answers); $i++) {
                if($userData -> answers[$i] -> snippet == $lastDone){
                    $hasMissed = false;
                    break;
                }
            }
            if($hasMissed && count($userData -> snippetOrder) > 0){
                //Question missed, need to pop one so the right paradigm is used
                array_pop($snipLeft);

                if($userData->subjectDetails->reactExp != "none"){
                    array_pop($snipLeft);
                }
            }

            //get next
            $next["showNext"] = array_pop($snipLeft);
            array_push($userData -> snippetOrder,$next["showNext"]);

            //Check for empty next
            if($next["showNext"] == null){
                $next["done"] = $totQuestions;     //signals its over
            }
        }else{
            //clear remaining, not needed
            $snipLeft = array();
        }

        //save back without the popped snippet code
        $userData->snippetsLeft = $snipLeft;
        file_put_contents($filePath.$subjectID.$actionType .'.json', json_encode($userData));

        echo json_encode($next);
        break;
    case 'result':
        $actionType = '_calibration';

        $subjectID = $_GET['subID'];

        //read the file
        //$userData = json_decode(file_get_contents(__DIR__.$filePath.$subjectID .'.json', true));
        $userData = json_decode(file_get_contents($filePath.$subjectID.$actionType .'.json', true));

        $result["done"] = 1;
        $result["correct"] = 1;
        echo json_encode($result);
        break;
}

function calculatePrecision($data){
    $euDistSum = 0;

    for($dot = 0; $dot < count($data); $dot++){
        if(count($data[$dot]->gazeData) == 0){
            //Data missing, return unreasonably large value to deliberately fail calibration
            return 3000;
        }

        $x = $data[$dot]->xPos;
        $y = $data[$dot]->yPos;

        $allXGaze = 0;
        $allYGaze = 0;

        for($gaze = 0; $gaze < count($data[$dot]->gazeData); $gaze++){
            $allXGaze = $allXGaze + $data[$dot]->gazeData[$gaze]->x;
            $allYGaze = $allYGaze + $data[$dot]->gazeData[$gaze]->y;
        }

        $avgXGaze = $allXGaze / count($data[$dot]->gazeData);
        $avgYGaze = $allYGaze / count($data[$dot]->gazeData);

        $euDistSum = $euDistSum + sqrt(pow(abs($x-$avgXGaze), 2) + pow(abs($y-$avgYGaze), 2));
    }

	return $euDistSum/count($data);
}

/*function correctAnswer($snippet, $givenAnswer){
    //strip spaces and make lower case
    $stripped = strtolower (preg_replace('/\s+/', '', $givenAnswer));

    switch ($snippet){
        case "1I":
            //Printer = "aborted"
            return $stripped == "aborted" ? true : false;
        case "2I":
            //IntStatistics	 = 5
            return $stripped == "5" ? true: false;
        case "3I":
            //CheckIfLettersOnly = No
            return $stripped == "no" ? true: false;
        case "4I":
            //InsertSort = 7543
            return $stripped == "7543" ? true: false;
        case "5I":
            //ReverseString = dlrow olleH
            return $stripped == "dlrowolleh" ? true: false;
        case "6I":
            //Count
            return $stripped == "3" ? true: false;
        case "7I":
            //InheritAnimal = RuffRuff
            return $stripped == "ruffruff" ? true: false;
        case "8I":
            //Triangle
            return $stripped == "4.5" ? true: false;
        case "9I":
            //SumArray = 23
            return $stripped == "23" ? true: false;
        case "10I":
            //Transform
            return $stripped == "5343" ? true: false;
        case "11I":
            //Sort
            return $stripped == "1459" ? true: false;
        case "12I":
            //Remove
            return $stripped == "1102345" ? true: false;
        case "13I":
            //Box
           return $stripped == "284125" ? true: false;
        case "14I":
            //Numbers
            return $stripped == "67" ? true: false;
        case "15I":
            //ListValue
            return $stripped == "752" ? true: false;
        case "1R":
            //Ambiguous
            return $stripped == "catcatcatcat" ? true: false;
        case "2R":
            //ColorSub
            return $stripped == "345" ? true: false;
        case "3R":
            //DistNum
            return $stripped == "onethreefour" ? true: false;
        case "4R":
            //TwiceScheduled
            return $stripped == "sum:691" ? true: false;
        case "5R":
            //ThreeDiv
            return $stripped == "3691215" ? true: false;
        case "6R":
            //StopAndUnsubscribe
            return $stripped == "123" ? true: false;
        case "7R":
            //Groceries
            return $stripped == "milktuna" ? true: false;
        case "8R":
            //Group
            return $stripped == "3" ? true: false;
        case "9R":
            //Stop
            return $stripped == "readyset" ? true: false;
        case "10R":
            //SameTime
            return $stripped == "0123344" ? true: false;
        case "11R":
            //Rectangle
            return $stripped == "100100" ? true: false;
        case "12R":
            //Range
            return $stripped == "21" ? true: false;
        case "13R":
            //PrimeNumbers
            return $stripped == "11131719" ? true: false;
        case "14R":
            //ObserverNumbers
            return $stripped == "3359fail" ? true: false;
        case "15R":
            //ParallellColor
            return $stripped == "1red2blue3orange" ? true: false;
        case "1S":
            //What did the frog offer to offer for the princess?
            if (in_array($stripped, array("goldenball", "goldball", "ball", "plaything", "toy"))) {
               return true;
            }
            return false;
        case "2S":
            //How does the mother descript the wolfs voice?
            return $stripped == "rough" ? true : false;
        case "3S":
            //What did Rapunzel do to get the king's sons attention?
            if (in_array($stripped, array("shesang", "sang", "song"))) {
                return true;
            }
            return false;
        case "4S":
            //What did the brother turn into when he drank the water?
            if (in_array($stripped, array("aroe", "roe", "deer", "animal"))) {
                return true;
            }
            return false;
        case "5S":
            //What two items did the girl give away?
            if (in_array($stripped, array("breadhood", "hoodbread", "breadandhood", "hoodandbread"))) {
                return true;
            }
            return false;
        case "6S":
            //How many ravens flew down to him?
            if (in_array($stripped, array("3", "three"))) {
                return true;
            }
            return false;
        case "7S":
            //What were the windows made out of?
            if (in_array($stripped, array("sugar", "clearsugar"))) {
                return true;
            }
            return false;
        case "8S":
            //What did the evil sisters empty into the ashes?
            if (in_array($stripped, array("peaslentils", "peasandlentils", "lentilspeas", "lentilsandpeas"))) {
                return true;
            }
            return false;
        case "9S":
            //What three trades did the sons decide to learn?
            return $stripped == "barber" ? true : false;
        case "10S":
            //To what town did the animals set out to become musicians?
            return $stripped == "bremen" ? true : false;
        case "11S":
            //What did Little Red-Cap bring to her grandmother?
            if (in_array($stripped, array("cakewine", "cakeandwine", "winecake", "wineandcake", "cakeandbottleofwine"))) {
                return true;
            }
            return false;
        case "12S":
            //What was the old woman doing on the ground?
            if (in_array($stripped, array("cuttinggrass", "cutgrass", "kneeling"))) {
                return true;
            }
            return false;
        case "13S":
            //What did the Little man turn the straw into?
            return $stripped == "gold" ? true : false;
        case "14S":
            //What was the fish made entirely out of?
            return $stripped == "gold" ? true : false;
        case "15S":
            //What was the Ash-Maidens own shoes made out of?
            if (in_array($stripped, array("wood", "heavywood", "wooden"))) {
                return true;
            }
            return false;
    }

    return false;
}

*/
