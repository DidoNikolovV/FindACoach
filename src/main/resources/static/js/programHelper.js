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

    console.log("This is the formData: ", formData);

    fetch(`${window.location.origin}/api/v1/program-helper/get-recommendation`, {
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
            // Hide all programs
            const allProgramCards = document.querySelectorAll('#programsContainer .col-md-4');
            allProgramCards.forEach(card => card.style.display = 'none');

            // Show only the recommended program
            const recommendedProgram = Array.from(allProgramCards).find(card => {
                const cardTitle = card.querySelector('.card-title').innerText.toLowerCase();
                return cardTitle.includes(data.fitnessLevel);
            });

            if (recommendedProgram) {
                recommendedProgram.style.display = 'block';
                recommendedProgram.classList.remove('col-md-4');
                recommendedProgram.classList.add('col-md-12');
            }

            // Update the recommended program details
            const recommendedProgramTitle = recommendedProgram.querySelector('.card-title').innerText;
            const recommendationMessage = document.createElement('p');
            recommendationMessage.className = 'recommended-program-message text-center';
            recommendationMessage.innerText = "Based on your input, we recommend you start with this program.";

            const titleElement = document.createElement('h4');
            titleElement.className = 'text-center text-info';
            titleElement.innerText = `Recommended Program: ${recommendedProgramTitle}`;

            // Append the recommendation message and title above the recommended program
            const programsContainer = document.getElementById('programsContainer');
            programsContainer.parentNode.insertBefore(recommendationMessage, programsContainer);
            programsContainer.parentNode.insertBefore(titleElement, programsContainer);

            // Hide the helper button container
            document.getElementById('helperButtonContainer').style.display = 'none';

            // Show the button to display all programs
            // document.getElementById('showAllProgramsButton').style.display = 'block';
        })
        .catch(err => {
            console.error(err);
        })
}