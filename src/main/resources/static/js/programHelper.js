const url = 'http://localhost:8080';

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content;
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content;

let recommendationBtn = document.getElementById("getRecommendationBtn");
console.log(recommendationBtn);
document.getElementById("getRecommendationBtn").addEventListener("click", loadRecommendation);
document.getElementById('showAllProgramsButton').addEventListener('click', showAllPrograms);

function loadRecommendation() {
    const formData = {
        fitnessLevel: document.getElementById('fitnessLevel').value,
        goal: document.getElementById('goal').value,
        workoutPreference: document.getElementById('workoutPreference').value,
        workoutFrequency: document.getElementById('workoutFrequency').value
    };

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
            // Hide all programs
            const allProgramCards = document.querySelectorAll('#programsContainer .col-md-4');
            allProgramCards.forEach(card => card.style.display = 'none');

            const recommendedProgram = Array.from(allProgramCards).find(card => {
                const cardTitle = card.querySelector('.card-title').innerText.toLowerCase();
                return cardTitle.includes(data.fitnessLevel);
            });

            if (recommendedProgram) {
                recommendedProgram.style.display = 'block';
                recommendedProgram.classList.remove('col-md-4');
                recommendedProgram.classList.add('col-md-12');
            }

            const recommendedProgramTitle = recommendedProgram.querySelector('.card-title').innerText;
            const recommendationMessage = document.createElement('p');

            const titleElement = document.createElement('h4');
            titleElement.className = 'text-center text-white';
            titleElement.innerText = `Recommended Program: ${recommendedProgramTitle}`;

            // Append the recommendation message and title above the recommended program
            const programsContainer = document.getElementById('programsContainer');
            programsContainer.parentNode.insertBefore(recommendationMessage, programsContainer);
            programsContainer.parentNode.insertBefore(titleElement, programsContainer);

            // Hide the helper button container
            document.getElementById('helperButtonContainer').style.display = 'none';

            // Show the button to display all programs
            document.getElementById('showAllProgramsRow').style.display = 'block';

        })
        .catch(err => {
            console.error(err);
        });
}

function showAllPrograms() {

    const allProgramCards = document.querySelectorAll('#programsContainer .col-md-4, #programsContainer .col-md-12');
    allProgramCards.forEach(card => {
        card.style.display = 'block';
        card.classList.remove('col-md-12');
        card.classList.add('col-md-4');
    });


    document.getElementById('helperButtonContainer').style.display = 'block';
    document.getElementById('showAllProgramsRow').style.display = 'none';

}