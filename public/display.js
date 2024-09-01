const addButtonOne = document.getElementById('addMoreCourses');
const addButtonTwo = document.getElementById('addMoreTakenCourses');
const parentForAll = document.getElementById('parentForMoreInput');
const ContainerForSchedule = document.getElementById('potentialCourses');
const ContainerForEligible = document.getElementById('takenCourses');
const addHereFirst = document.getElementById('firstAdd');
const addHereSecond = document.getElementById('secondAdd');

const firstScheduleInput = document.getElementById('firstScheduleInput');
var inputScheduleArray = [];
const firstEligibleInput = document.getElementById('firstEligibleInput');
var inputEligibleArray = [];


var scheduleCounter = 0;
addButtonOne.addEventListener('click', function() {
    console.log("works, clicked");
    const anotherContainer = document.createElement('div');
    const deleteContainer = document.createElement('span');
    const deleteButton = document.createElement('button');
    const paragraphForIndent = document.createElement('p');
    deleteButton.textContent = "Remove";
    const textBox = document.createElement('input');
    scheduleCounter++;
    textBox.setAttribute('type', 'text');
    textBox.setAttribute('name', 'moreInputForSchedule');
    textBox.style.marginTop = "0px";
    textBox.required = true;
    paragraphForIndent.style.marginLeft = "50px";
    paragraphForIndent.style.marginTop = "1px";
    paragraphForIndent.style.marginBottom = "1px";
    paragraphForIndent.appendChild(textBox);
    paragraphForIndent.appendChild(deleteButton);
    anotherContainer.appendChild(deleteContainer);
    deleteContainer.appendChild(paragraphForIndent);
    let textBoxVal = textBox.value;
    addHereFirst.appendChild(anotherContainer);
    deleteButton.addEventListener('click', function() {
        anotherContainer.remove();
        scheduleCounter--;
    })
    
})


var eligibleCounter = 1;
addButtonTwo.addEventListener('click', function() {
    const anotherContainer = document.createElement('div');
    const deleteContainer = document.createElement('span');
    const deleteButton = document.createElement('button');
    deleteButton.textContent = "Remove";
    const textBox = document.createElement('input');
    eligibleCounter++;
    textBox.setAttribute('type', 'text');
    textBox.setAttribute('name', 'moreInputForEligible');
    textBox.required = true;
    anotherContainer.appendChild(deleteContainer);
    deleteContainer.appendChild(textBox);
    deleteContainer.appendChild(deleteButton);
    //anotherContainer.style.display = "inline-block";
    addHereSecond.appendChild(anotherContainer);
    deleteButton.addEventListener('click', function() {
        anotherContainer.remove()
        eligibleCounter--;
    })
})

function hash(inputScheduleArray) {
    const map = new Map();
    for(var i = 0; i < inputScheduleArray.length; i++) {
        map.set(inputScheduleArray[i], i);
    }

    if(map.size == inputScheduleArray.length) {
        return true;
    } else {
        return false;
    }
}

//fetches data when pages load
let classDatabase = [];

async function loadDatabase() {
    try {
        const response = await fetch("http://localhost:5000/loadDatabase");
        const data = await response.json();
        classDatabase = [...data];
    } catch (error) {
        console.log("error: " , error);
    }
}

loadDatabase().then(() => {
console.log(classDatabase);
const form = document.querySelector('.formOne');
form.addEventListener('submit', (e) => {
    e.preventDefault();
    console.log(inputScheduleArray);
    const fd = new FormData(form);
    for(let [key, value] of fd.entries()) {
        if(!classDatabase.includes(value)) {
            alert("Class not found");
            return;
        }
    }
    for(let [key, value] of fd.entries()) {
        inputScheduleArray.push(value);
    }
    if(!hash(inputScheduleArray)) {
        console.log(inputScheduleArray);
        alert('This value has already been submitted. Please enter a different value.');
        resetForm();
        return;
    }
    fd.append('scheduleCounter', scheduleCounter);
    const urlEncoded = new URLSearchParams(fd).toString();
    fetch("http://localhost:5000/formOne", {
        method: "POST",
        body: urlEncoded,
        headers: {
            "Content-type": "application/x-www-form-urlencoded",
        },
    }).then(res => res.text())
    .then(data => {
        downloadSchedule(data);
        resetForm();
    })

    .catch(err => console.error('Error:', err));
});

const formTwo = document.querySelector('.formTwo');
formTwo.addEventListener('submit', (e) => {
    e.preventDefault();
    const fd = new FormData(formTwo);
    for(let [key, value] of fd.entries()) {
        if(!classDatabase.includes(value)) {
            alert("Class not found");
            return;
        }
    }
    for(let [key, value] of fd.entries()) {
        inputEligibleArray.push(value);
    }
    if(!hash(inputEligibleArray)) {
        console.log(inputEligibleArray);
        alert('This value has already been inputted. Please enter a different value.');
        resetForm();
        return;
    }
    fd.append('eligibleCounter', eligibleCounter); //adds eligibleCounter to form data (req.body)
    const urlEncoded = new URLSearchParams(fd).toString();
    fetch("http://localhost:5000/formTwo", {
        method: "POST",
        body: urlEncoded,
        headers: {
            "Content-type": "application/x-www-form-urlencoded",
        },
    }).then(res => res.json()) //may change later
    .then(data => download(data))
    .catch(err => console.error('Error:', err));
})


function download(data) {
    try {
        var file = new Blob([data], {type:"text/plain"});
        const a = document.createElement('a');
        const url = URL.createObjectURL(file); // response.data for axios
        a.href = url;
        a.download = 'eligible.out';
        a.style.display = 'none';
        document.body.appendChild(a);
        a.click();
        a.remove();
        URL.revokeObjectURL(url);
    } catch (error) {
        console.log("Error during download:", error);
    }
}


function downloadSchedule(data) {
    try {
        // Replace literal '\n' with actual newline characters
        var file = new Blob([data], {type: "text/plain"});
        const a = document.createElement('a');
        const url = URL.createObjectURL(file);
        a.href = url;
        a.download = 'scheduleplan.out';
        a.style.display = 'none';
        document.body.appendChild(a);
        a.click();
        a.remove();
        URL.revokeObjectURL(url);
    } catch (error) {
        console.log("Error during download:", error);
    }
}

function resetForm() {
    inputScheduleArray = [];
    inputEligibleArray = [];
    console.log("form reset");
}
})


