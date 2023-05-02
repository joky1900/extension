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