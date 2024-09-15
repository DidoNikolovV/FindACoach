const url = 'http://localhost:8080';

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content;
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content;

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
    })
        .then(res => {
            if (!res.ok) {
                throw new Error('Network response was not ok');
            }
            return res.json();
        })
        .then(data => {
            console.log(data);
            if (!data || !data.name) {
                console.log('No program found for the given criteria.');
                return;
            }

            // Hide all programs initially
            const allProgramCards = document.querySelectorAll('#programsContainer .col');
            allProgramCards.forEach(card => {
                card.style.display = 'none';
            });

            // Show the recommended program
            const recommendedProgram = Array.from(allProgramCards).find(card => {
                const cardTitle = card.querySelector('.card-title').innerText.toLowerCase();
                return cardTitle.includes(data.name.toLowerCase());
            });

            console.log(recommendedProgram);

            if (recommendedProgram) {
                recommendedProgram.style.display = 'block';
                recommendedProgram.classList.remove('col-md-4');
                recommendedProgram.classList.add('col-md-4');
                recommendedProgram.classList.add('recommended-program');
                // Keep original size

                // Adjust the image style for the recommended program
                const programImg = recommendedProgram.querySelector('.program-img');
                if (programImg) {
                    programImg.style.height = 'auto'; // Remove fixed height
                    programImg.style.objectFit = 'cover'; // Ensure the image covers its container
                }

                // Create and insert the recommendation message
                const titleElement = document.createElement('h4');
                titleElement.className = 'text-center text-white mb-4';
                titleElement.innerText = `Recommended Program: ${data.name}`;

                const programsContainer = document.getElementById('programsContainer');
                const existingRecommendationMessage = document.querySelector('#recommendationMessage');
                if (existingRecommendationMessage) {
                    existingRecommendationMessage.remove();
                }

                const recommendationMessage = document.createElement('p');
                recommendationMessage.id = 'recommendationMessage';
                recommendationMessage.className = 'text-center text-white mb-4';
                recommendationMessage.innerText = `Based on your preferences, we recommend the following program:`;

                programsContainer.parentNode.insertBefore(titleElement, programsContainer);
                programsContainer.parentNode.insertBefore(recommendationMessage, programsContainer);
            }

            document.getElementById('helperButtonContainer').style.display = 'none';
            document.getElementById('showAllProgramsRow').style.display = 'block';

            $('#helperModal').modal('hide');
        })
        .catch(err => {
            console.error('Error fetching recommendation:', err);
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
