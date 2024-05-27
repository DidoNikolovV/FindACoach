const url = 'http://localhost:8080'

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

// Fetch exercises when the modal is opened
$('#exerciseModal').on('show.bs.modal', function () {
    fetchExercises();
});

function fetchExercises() {
    fetch(`${url}/api/v1/exercises`, {
        headers: {
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        }
    })
        .then(res => res.json())
        .then(data => {
            console.log("Entered: " + data)
            const exerciseList = document.getElementById('exerciseList');
            exerciseList.innerHTML = '';
            data.forEach(exercise => {
                const card = document.createElement('div');
                card.className = 'col-md-4 mb-3';
                card.innerHTML = `
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">${exercise.name}</h5>
                                <input type="checkbox" class="form-check-input" id="exercise-${exercise.id}" value="${exercise.id}">
                                <label class="form-check-label" for="exercise-${exercise.id}">Select</label>
                            </div>
                        </div>
                    `;
                exerciseList.appendChild(card);
            });
        })
        .catch(error => {
            console.error('Error fetching exercises:', error);
        });
}

function saveSelectedExercises() {
    const selectedExercises = [];
    document.querySelectorAll('#exerciseList input[type="checkbox"]:checked').forEach(checkbox => {
        selectedExercises.push({
            id: checkbox.value,
            name: checkbox.parentElement.querySelector('.card-title').textContent
        });
    });

    const selectedExercisesList = document.getElementById('selectedExercises');
    selectedExercisesList.innerHTML = '';
    selectedExercises.forEach(exercise => {
        const listItem = document.createElement('li');
        listItem.className = 'list-group-item';
        listItem.textContent = exercise.name;
        listItem.innerHTML += `
                <input type="hidden" name="selectedExerciseIds" value="${exercise.id}">
                <div class="form-group">
                    <label for="sets-${exercise.id}">Sets</label>
                    <input type="number" class="form-control" id="sets-${exercise.id}" name="sets-${exercise.id}">
                </div>
                <div class="form-group">
                    <label for="reps-${exercise.id}">Reps</label>
                    <input type="number" class="form-control" id="reps-${exercise.id}" name="reps-${exercise.id}">
                </div>
            `;
        selectedExercisesList.appendChild(listItem);
    });

    $('#exerciseModal').modal('hide');
}