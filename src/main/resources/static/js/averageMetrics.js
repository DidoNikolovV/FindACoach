const url = 'http://localhost:8080'

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

document.getElementById("dataModalBtn").addEventListener("click", openDataModal);

function openDataModal() {
    const clientName = document.getElementById("clientName").value;
    console.log("Entered...");

    fetch(`${url}/api/v1/clients/${clientName}/metrics/data`, {
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
        for(let data of res) {
            document.getElementById("avgWeight").innerText = data.weight + " kg";
            document.getElementById("avgStepsCount").innerText = data.stepsCount;
            document.getElementById("avgSleep").innerText = data.sleepDuration + " hours";
            document.getElementById("avgMood").innerText = convertMood(data.mood);
            document.getElementById("avgEnergyLevels").innerText = convertEnergyLevels(data.energyLevels);
        }
        // Populate the modal fields with data from the response

        $('#weeklyDataModal').modal('show');
    }).catch(function(error) {
        console.error('Error:', error.message);
    });
}

// Helper functions to convert mood and energyLevels integer values to strings
function convertMood(mood) {
    // Implement your logic here to convert mood integer to string
    // Example logic:
    if (mood === 1) {
        return "Poor";
    } else if (mood === 2) {
        return "Fair";
    } else if (mood === 3) {
        return "Good";
    } else if (mood === 4) {
        return "Excellent";
    } else {
        return "Unknown";
    }
}

function convertEnergyLevels(energyLevels) {
    // Implement your logic here to convert energyLevels integer to string
    // Example logic:
    if (energyLevels === 1) {
        return "Low";
    } else if (energyLevels === 2) {
        return "Moderate";
    } else if (energyLevels === 3) {
        return "High";
    } else {
        return "Unknown";
    }
}