const url = 'http://localhost:8080'

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

const modal = document.getElementById("openModal");
modal.addEventListener("click", openModal);
document.getElementById("scheduleButton").addEventListener("click", scheduleWorkout);

function openModal() {
    console.log("opening modal....")
    $('#workoutModal').modal('show');
}

function scheduleWorkout(e) {
    e.preventDefault();
    var workoutDate = document.getElementById("workoutDate").value;
    var coachUsername = document.getElementById("coachUsername").value;
    var clientUsername = document.getElementById("clientUsername").value;
    console.log(coachUsername);

    var formData = {
        workoutDate: workoutDate
    };

    fetch(`${url}/api/v1/schedule-workouts/${coachUsername}`, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            console.log(response)
            if (response.ok) {
                alert("Workout scheduled successfully!");
            } else {
                console.error("Failed to schedule workout:", response.statusText);
            }
            $('#workoutModal').modal('hide')
            window.location.href = `${url}/users/${clientUsername}/calendar`
        })
        .catch(error => {
            console.error("Error scheduling workout:", error);
            $('#workoutModal').modal('hide');
        });
}