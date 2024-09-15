const url = 'http://localhost:8080'

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content
document.getElementById("openModalBtn").addEventListener("click", openModal);
const programId = document.getElementById("programId").value;

let completedWeeks = [];

function openModal() {
    let selectedWeek = document.getElementById("weekSelect").value;
    fetchWorkoutsForWeek(selectedWeek);
}

function fetchWorkoutsForWeek(week) {
    fetch(`${url}/api/v1/workouts`, {
        headers: {
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        }
    }).then(res => {
        if (!res.ok) {
            throw new Error('Failed to fetch data');
        }
        return res.json();
    }).then(workouts => {
        console.log(workouts);

        const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
        const modalBody = document.querySelector("#workoutModal .modal-body");
        modalBody.innerHTML = "";

        days.forEach(day => {
            const dayLabel = document.createElement("label");
            dayLabel.appendChild(document.createTextNode(day + ":"));

            const selectMenu = document.createElement("select");
            selectMenu.name = day; // Ensure day is correctly set
            selectMenu.className = "form-control";

            workouts.forEach(workout => {
                const option = document.createElement("option");
                option.setAttribute('data-id', workout.id);
                option.setAttribute("data-day", day);
                option.value = workout.name;
                option.text = workout.name;
                selectMenu.appendChild(option);
            });

            const br = document.createElement("br");
            modalBody.appendChild(dayLabel);
            modalBody.appendChild(selectMenu);
            modalBody.appendChild(br);
        });

        $('#workoutModal').modal('show');
    }).catch(error => {
        console.error('Error fetching data:', error);
    });
}

document.getElementById("saveWorkoutsBtn").addEventListener("click", saveWorkouts);

function saveWorkouts(e) {
    e.preventDefault();
    const modalBody = document.querySelector("#workoutModal .modal-body");
    const workoutsToAdd = [];
    const selectedWeek = document.getElementById("weekSelect").value;

    modalBody.querySelectorAll("select").forEach(selectMenu => {
        const selectedOption = selectMenu.options[selectMenu.selectedIndex];
        const selectedId = selectedOption.getAttribute('data-id');
        const selectedDay = selectMenu.name; // Ensure this is correctly set
        const selectedName = selectMenu.value;

        const workout = {
            id: selectedId,
            name: selectedDay,
            workoutName: selectedName
        };

        workoutsToAdd.push(workout);
    });

    fetch(`${url}/api/v1/workouts/programs/${programId}/weeks/${selectedWeek}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify(workoutsToAdd)
    }).then(res => res.json())
        .then(data => {
            console.log(data);
            // completedWeeks.push(selectedWeek);
            updateWeekSelect();
        }).catch(error => {
        console.error('Error saving data:', error);
    });

    $('#workoutModal').modal('hide');
}

// Function to update the week dropdown
function updateWeekSelect() {
    const weekSelect = document.getElementById("weekSelect");
    const currentWeek = weekSelect.value;
    const options = Array.from(weekSelect.options);
    // Remove completed weeks from options
    options.forEach(option => {
        if (completedWeeks.includes(option.value)) {
            option.remove();
        }
    });
    // Select the next available week
    const nextWeekOption = options.find(option => !completedWeeks.includes(option.value));
    if (nextWeekOption) {
        weekSelect.value = nextWeekOption.value;
    }
}

