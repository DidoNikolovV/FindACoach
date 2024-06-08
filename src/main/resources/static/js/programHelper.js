const url = 'http://localhost:8080';

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content;
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content;

let recommendationBtn = document.getElementById("getRecommendationBtn");
console.log(recommendationBtn);
document.getElementById("getRecommendationBtn").addEventListener("click", loadRecommendation);

function loadRecommendation() {

    const formData = {
        fitnessLevel: document.getElementById('fitnessLevel').value,
        goal: document.getElementById('goal').value,
        workoutPreference: document.getElementById('workoutPreference').value,
        workoutFrequency: document.getElementById('workoutFrequency').value
    };

    console.log("THis is the formData: " + formData);

    fetch(`${url}/api/v1/program-helper/get-recommendation`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify(formData)
    }).then(res => res.json())
        .then(data => {
            console.log(data);
        }
    ).catch(err => {
        console.error(err);
    })
}