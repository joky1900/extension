# <font color="orange">Benchmarking Extension for EyesOnTheCode Software </font> 

##  <font color="green">About the benchmarking: </font>

This project is a benchmarking tool designed to assess the accuracy of Eye-Tracking software.    
It is an extension of the existing "EyesOnTheCode" program with benchmarking functionality,    
created by two students who aimed to provide a valuable service to Eye-Tracking software participants.   


## <font color="green">Usage: </font>

On the main page, participant can find two buttons: "Eye Tracking Study" and "Benchmarking Extension".    
Clicking on "Benchmarking Extension" will start the tool but by clicking on "Eye Tracking Study"    
will redirect to the program for which this tool was developed.    

Before starting the test, calibration is necessary. During calibration, the participant should look    
at small circles with numbers inside that appear on the screen. When the number reaches 0,    
a new circle will appear in a different place on the screen.      

Throughout the entire test, access to the camera is necessary, and the camera should be positioned    
at eye level for the participant. Only researcher can modify the test parameters in the  <font color="green">config.json file</font>,    
and <font color="green">THE DEMO</font> can immediately show those changes.       


````
{
  "ballSpeed": 6,
  "randomMoves": "no",
  "randomStart": "no",
  "exTime": 60,
  "backgroundImg": "backgroundWater.jpg",
  "desiredIntervalTime": 30
}    
````



- <font color="orange">ballSpeed: </font>       
The speed of the moving ball in the benchmarking test.    
The default value is <font color="blue">6 </font>.   
- <font color="orange">randomMoves: </font>   
Determines if the ball moves randomly or follows a set trajectory.   
The default value is <font color="red">"no" </font>.   
- <font color="orange">randomStart: </font>    
Determines if the ball starts at a random position or a fixed position.      
The default value is <font color="red">"no" </font>.  
- <font color="orange">exTime: </font>   
The duration of the benchmarking test in seconds.   
The default value is <font color="blue">30 </font>.   
- <font color="orange">backgroundImg: </font>   
The name of the background image to be used in the benchmarking test.    
The default value is "backgroundWater.jpg".    
The configuration contains four background pictures to choose:   

````
|--- backgroundIMG
|     |--- backgroundCommodore.png
|     |--- backgroundDesert.png
|     |--- backgroundStars.png
|     |--- backgroundWater.png 
````   

- <font color="orange">desiredIntervalTime: </font>    
The time in milliseconds between each movement of the dot in the benchmarking test.     
The default value is <font color="blue">30 </font>.     

These parameters can be modified to customize the benchmarking test according to     
the researcher's preferences.

The benchmarking extension introduces a ball that changes its position on the screen during the test,    
supplementing the lack of tracking during dynamic actions. It checks the efficiency and the accuracy   
of the algorithm when the ball is being tracked. Options to modify the benchmark are described above.   

After the test, the Data Analysis Tool can be used to import JSON data collected through    
the benchmarking extension and create graphical elements, including the ability to change    
graph types, axes, and graphical profiles. Researchers can also export the data as CSV for    
further extendability.   

## <font color="green">License: </font>   

The source code is published on GitHub, under the GNU General Public License,    
and is accessible for free. We appreciate any feedback or suggestions you may have.   