const url = 'http://localhost:8080'

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content
const clientWorkoutDetailsBtn = document.getElementById("clientWorkoutDetailsBtn");
clientWorkoutDetailsBtn.addEventListener("click", openModal);


function workoutDetailsAsHTML(workout) {
    const workoutNameElement = document.getElementById("workoutName");
    const workoutDescriptionElement = document.getElementById("workoutDescription");

    workoutNameElement.textContent = workout.name;
    workoutDescriptionElement.textContent = workout.description; // Assuming description is a property of workout

    return '';
}


function openModal() {
    const clientName = document.getElementById("clientName").value;
    const workoutId = document.getElementById("clientWorkoutDetailsBtn").dataset.workoutId;
    const dayName = document.getElementById("clientWorkoutDetailsBtn").dataset.workoutName;

    fetch(`${url}/api/v1/clients/completed-workouts/${clientName}/workouts/${workoutId}/${dayName}/details`, {
        headers: {
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        }
    }).then(res => {
        if (!res.ok) {
            throw new Error('Failed to fetch data');
        }
        return res.json();
    }).then(res => {
        console.log(res);
        const id = res.id;
        const name = res.name;

        document.getElementById("workoutName").innerHTML = `
            <h5>Workout Name: ${name}</h5>
        `;
        document.getElementById("workoutDescription").innerHTML = `
            <p>Workout ID: ${id}, Workout Name: ${name}</p>
        `;

        $('#workoutDetailsModal').modal('show');
    }).catch(function(error) {
        console.error('Error:', error.message);
    });
}



