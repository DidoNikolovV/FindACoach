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

        $('#weeklyDataModal').modal('show');
    }).catch(function(error) {
        console.error('Error:', error.message);
    });
}

function convertMood(mood) {
    if (mood >= 1 && mood < 4) {
        return "Poor";
    } else if (mood >= 4 && mood < 6) {
        return "Fair";
    } else if (mood >= 6 && mood < 9) {
        return "Good";
    } else if (mood >= 9 && mood <= 10) {
        return "Excellent";
    } else {
        return "Unknown";
    }
}

function convertEnergyLevels(energyLevels) {

    if (energyLevels >= 1 && energyLevels < 4) {
        return "Low";
    } else if (energyLevels >= 4 && energyLevels <= 7) {
        return "Moderate";
    } else if (energyLevels > 7 && energyLevels <= 10) {
        return "High";
    } else {
        return "Unknown";
    }
}