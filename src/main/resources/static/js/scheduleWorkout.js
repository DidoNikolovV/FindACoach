const url = 'http://localhost:8080'

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content


// const scheduleForm = document.getElementById("scheduleForm");
// scheduleForm.addEventListener("submit", scheduleWorkout);


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
    // Submit the form

    // Prepare the data to send to the server
    var formData = {
        workoutDate: workoutDate
    };

    // Send a POST request to your server to save the selected date
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
                // Optionally, you can perform further actions here, such as displaying a success message to the user
            } else {
                console.error("Failed to schedule workout:", response.statusText);
                // Optionally, you can handle error cases here, such as displaying an error message to the user
            }
            // Close the modal regardless of the response status
            $('#workoutModal').modal('hide')
            window.location.href = `${url}/users/${clientUsername}/calendar`
        })
        .catch(error => {
            console.error("Error scheduling workout:", error);
            // Optionally, you can handle errors here, such as displaying an error message to the user
            // Close the modal regardless of the error
            $('#workoutModal').modal('hide');
        });
}