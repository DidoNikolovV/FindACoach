const url = 'http://localhost:8080';

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content;
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content;

document.getElementById("dataModalBtn").addEventListener("click", openDataModal);

function openDataModal() {
    const clientName = document.getElementById("clientName").value;
    console.log("Fetching data for client:", clientName);

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
    }).then(data => {
        console.log(data);
        updateWeeklyDataTable(data);
        updateWeeklyDataCharts(data);
        $('#weeklyDataModal').modal('show');
    }).catch(error => {
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

function updateWeeklyDataTable(data) {
    const weeklyDataBody = document.getElementById('weeklyDataBody');
    if (!weeklyDataBody) {
        console.error('weeklyDataBody element not found');
        return;
    }
    weeklyDataBody.innerHTML = '';

    data.forEach((week, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>Week ${index + 1}</td>
            <td>${week.avgWeight} kg</td>
            <td>${week.avgStepsCount}</td>
            <td>${week.avgSleepDuration} hours</td>
            <td>${convertMood(week.avgMood)}</td>
            <td>${convertEnergyLevels(week.avgEnergyLevels)}</td>
        `;
        weeklyDataBody.appendChild(row);
    });
}

function updateWeeklyDataCharts(data) {
    const chartConfigs = [
        {elementId: 'week1Chart', data: data[0]},
        {elementId: 'week2Chart', data: data[1]},
        {elementId: 'week3Chart', data: data[2]},
        {elementId: 'week4Chart', data: data[3]}
    ];

    chartConfigs.forEach(config => {
        const canvasElement = document.getElementById(config.elementId);
        if (!canvasElement) {
            console.error(`Canvas element with ID ${config.elementId} not found`);
            return;
        }
        const ctx = canvasElement.getContext('2d');
        if (!ctx) {
            console.error(`Unable to get 2D context for canvas with ID ${config.elementId}`);
            return;
        }

        const weightData = config.data ? config.data.weight : [];
        const stepsCountData = config.data ? config.data.stepsCount : [];
        const sleepDurationData = config.data ? config.data.sleepDuration : [];
        const moodData = config.data ? config.data.mood : [];
        const energyLevelsData = config.data ? config.data.energyLevels : [];
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['Day 1', 'Day 2', 'Day 3', 'Day 4', 'Day 5', 'Day 6', 'Day 7'],
                datasets: [
                    {
                        label: 'Weight',
                        data: [weightData, 73, 70, 80],
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1,
                        fill: true
                    },
                    {
                        label: 'Steps Count',
                        data: [stepsCountData, 10000, 9500, 73000],
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1,
                        fill: false
                    },
                    {
                        label: 'Sleep Duration',
                        data: [sleepDurationData, 8, 5, 7],
                        borderColor: 'rgba(255, 206, 86, 1)',
                        borderWidth: 1,
                        fill: false
                    },
                    {
                        label: 'Mood',
                        data: [moodData, 6, 10, 9],
                        borderColor: 'rgba(153, 102, 255, 1)',
                        borderWidth: 1,
                        fill: false
                    },
                    {
                        label: 'Energy Levels',
                        data: [energyLevelsData, 8, 9, 10],
                        borderColor: 'rgba(255, 159, 64, 1)',
                        borderWidth: 1,
                        fill: false
                    }
                ]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    });
}

