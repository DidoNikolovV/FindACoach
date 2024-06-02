const url = 'http://localhost:8080'

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content
const exercisesModalButton = document.getElementById("exerciseModalButton");
document.getElementById("createWorkout").addEventListener("click", createWorkout);

$('#exerciseModal').on('show.bs.modal', function () {
    fetchExercises();
});

let selectedExercises = [];

let currentPage = 0;

function fetchExercises(page = 0) {
    const pageSize = 6;
    fetch(`${url}/api/v1/workout?page=${page}&size=${pageSize}`, {
        headers: {
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        }
    })
        .then(res => res.json())
        .then(data => {
            const exerciseList = document.getElementById('exerciseList');
            exerciseList.innerHTML = '';
            console.log(data.content);
            data.content.forEach(exercise => {
                const card = document.createElement('div');
                card.className = 'col-md-4 mb-3';
                card.innerHTML = `
                <div class="card h-100 shadow-sm" style="border-radius: 10px;">
                    <div class="card-body d-flex flex-column justify-content-between">
                        <div class="d-flex justify-content-between align-items-start mb-3">
                            <div>
                                <h5 class="card-title" style="font-size: 1rem;">${exercise.name}</h5>
                                <p class="card-text text-muted" style="font-size: 0.75rem;" id="muscleGroup">${exercise.muscleGroup}</p>
                            </div>
                            <div>
                                <input type="checkbox" class="form-check-input" id="exercise-${exercise.id}" value="${exercise.id}">
                                <label class="form-check-label" for="exercise-${exercise.id}" style="font-size: 0.85rem;">Select</label>
                            </div>
                        </div>
                        <div class="mt-auto d-flex flex-column align-items-end">
                            <div class="form-group mb-2">
                                <label for="sets-${exercise.id}" class="form-label" style="font-size: 0.75rem;">Sets</label>
                                <input type="number" id="sets-${exercise.id}" class="form-control form-control-sm" size="5" style="width: 60px; font-size: 0.75rem; padding: 0.25rem 0.5rem;">
                            </div>
                            <div class="form-group mb-0">
                                <label for="reps-${exercise.id}" class="form-label" style="font-size: 0.75rem;">Reps</label>
                                <input type="number" id="reps-${exercise.id}" class="form-control form-control-sm" size="5" style="width: 60px; font-size: 0.75rem; padding: 0.25rem 0.5rem;">
                            </div>
                        </div>
                    </div>
                </div>
            `;
                exerciseList.appendChild(card);
            });

            updatePagination(data.number, data.totalPages);
            $('#exerciseModal').modal('show');
            currentPage = page; // Update the currentPage variable
        })
        .catch(error => {
            console.error('Error fetching exercises:', error);
        });
}

function updatePagination(currentPage, totalPages) {
    const paginationList = document.querySelector('.pagination');
    paginationList.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const li = document.createElement('li');
        li.classList.add('page-item');
        if (i === currentPage) {
            li.classList.add('active');
        }

        const link = document.createElement('a');
        link.classList.add('page-link');
        link.href = '#';
        link.dataset.page = i;
        link.textContent = i + 1;

        li.appendChild(link);
        paginationList.appendChild(li);
    }

    // Add event listener to pagination links
    paginationList.querySelectorAll('.page-link').forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            const page = parseInt(this.dataset.page);
            fetchExercises(page);
        });
    });
}

document.getElementById("saveExercises").addEventListener("click", saveSelectedExercises);

function saveSelectedExercises() {
    console.log(">>>>>>>>>>>>>>>> saveSelectedExercises")
    const checkboxes = document.querySelectorAll('#exerciseList input[type="checkbox"]:checked');
    checkboxes.forEach(checkbox => {
        const exerciseId = checkbox.value;
        const exerciseName = checkbox.closest('.card-body').querySelector('.card-title').textContent;
        const sets = document.getElementById(`sets-${exerciseId}`).value;
        const reps = document.getElementById(`reps-${exerciseId}`).value;
        const muscleGroup = document.getElementById("muscleGroup").textContent;
        console.log(muscleGroup);

        const exercise = {
            id: exerciseId,
            name: exerciseName,
            sets: sets,
            reps: reps,
            muscleGroup
        };

        console.log(exercise);

        selectedExercises.push(exercise);
    });
    updateSelectedExercisesList();
    $('#exerciseModal').modal('hide');
}

function updateSelectedExercisesList(page = 1) {
    const selectedExercisesContainer = document.getElementById('selectedExercises');
    selectedExercisesContainer.innerHTML = '';
    selectedExercisesContainer.className = 'd-flex justify-content-between align-items-start'

    selectedExercises.forEach(exercise => {
        const listItem = document.createElement('li');
        listItem.className = 'list-group-item';
        listItem.innerHTML = `
            <div class="d-flex justify-content-between align-items-start">
                <div>
                    <h6 style="margin-bottom: 0.5rem;">${exercise.name}</h6>
                </div>
                <div>
                    <span class="badge badge-secondary" style="font-size: 0.85rem;">Muscle Group: ${exercise.muscleGroup}</span>
                </div>
            </div>
            <p class="mb-1" style="margin-bottom: 0.2rem;">
                <span style="display: inline-block; width: 50%;">Sets: ${exercise.sets}</span>
                <span style="display: inline-block; width: 50%;">Reps: ${exercise.reps}</span style="display: inline-block; width: 50%;">
            </p>
        `;
        selectedExercisesContainer.appendChild(listItem);
    });
}

function createWorkout() {
    console.log("=========Inside createWorkout function========")
    const workoutName = document.getElementById("name").value;
    const workoutLevel = document.getElementById("level").value;
    const workoutImg = document.getElementById("imgUrl").files[0];
    selectedExercises.forEach(exercise => console.log("Selected exercise: ", exercise));

    const formData = new FormData()
    formData.append("name", workoutName);
    formData.append("level", workoutLevel);
    formData.append("imgUrl", workoutImg);
    selectedExercises.forEach(exercise => {
        formData.append("exercises", JSON.stringify(exercise));
    })

    const workout = {
        name: workoutName,
        level: workoutLevel,
        imgUrl: workoutImg

    }
    fetch(`${url}/api/v1/workout/create`, {
        method: 'POST',
        body: formData
    }).then(res => res.json())
        .then(data => {
            console.log("Redirecting....")
            window.location.href = `${url}/workouts/${data.id}`
        })
        .catch(error => console.error(error));
}