const express = require("express");
const cors = require("cors");
const fs = require('fs');
const path = require('path');
const bodyParser = require('body-parser');
const axios = require("axios");
const app = express();

app.set("views", path.join(__dirname, 'views'));
app.set("view engine", "ejs");
app.use(cors({
    origin: 'http://localhost:5000',
    methods: ['GET', 'POST']
}));

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(path.join(__dirname, 'public')));


// Serve the homepage
app.get("/", (req, res) => {
    res.render("display"); 
});

app.post("/formOne", async (req, res) => {
    const classesToPlan = {
        classOne: req.body.targetCourse || '',
        scheduleCounter: req.body.scheduleCounter,
        classTwo: req.body.moreInputForSchedule || ''
    }
    let fileContent;
    if(classesToPlan.scheduleCounter >= 2) {
        fileContent = `${classesToPlan.classOne}\n${classesToPlan.scheduleCounter}\n${classesToPlan.classTwo.join('\n')}`;
    } else {
        fileContent = `${classesToPlan.classOne}\n${classesToPlan.scheduleCounter}\n${classesToPlan.classTwo}`;
    }

    //Save to Money dir
    const scheduleFilePath = path.join(__dirname, 'scheduleplan.in');
    fs.writeFile(scheduleFilePath, fileContent, (err) => {
        if(err) {
            console.error("Error: ", err);
        } else {
            console.log("file downloaded");
        }
    })

    //POST to java to run and return content
    const schedulebody = scheduleFilePath;
    axios.post(`http://localhost:8080/schedule?scheduleFilePath=${encodeURIComponent(schedulebody)}`) //need this to process post request
    .then(function (response) {
        res.setHeader('Content-Disposition', 'attachment; filename=scheduleplan.out');
        res.setHeader('Content-Type', 'text/plain');
        console.log(response.data);
        res.send(response.data)
        
    })
    .catch(function(err) {
        console.log("Error during POST request:", err);
    });
   
})

app.post("/formTwo", async (req, res) => {
    console.log(req.body);
    const classes = {
        eligibleCounter: req.body.eligibleCounter,
        classOne: req.body.twoBox || '', 
        classTwo: req.body.moreInputForEligible || ''
    };
    let fileContent;
    if(classes.eligibleCounter >= 3) {
        fileContent = `${classes.eligibleCounter}\n${classes.classOne}\n${classes.classTwo.join('\n')}`;
    } else if(classes.eligibleCounter == 1) {
        fileContent = `${classes.eligibleCounter}\n${classes.classOne}`;
    } else {
        fileContent = `${classes.eligibleCounter}\n${classes.classOne}\n${classes.classTwo}`;
    }
   
    // Save to the 'Money' directory
    const filePath = path.join(__dirname, 'eligible.in');
        fs.writeFile(filePath, fileContent, (err) => {
            if(err) {
                console.error("Error: ", err);
            } else {
                console.log("file downloaded");
            }
        })

    const body = filePath;
    axios.post(`http://localhost:8080/welcome?filePath=${encodeURIComponent(body)}`) //need this to process post request
    .then(function (response) {
        res.setHeader('Content-Disposition', 'attachment; filename=eligible.out');
        res.setHeader('Content-Type', 'text/plain');
        console.log(response.data);
        res.json(response.data)
        
    })
    .catch(function(err) {
        console.log("Error during POST request:", err);
    });


});





//change port back to 5000 if necessary
app.listen(5000, () => {
    console.log("Server is listening on port 5000"); 
});
