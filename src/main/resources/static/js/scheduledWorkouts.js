const url = 'http://localhost:8080'


const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

const calendar = document.getElementById('calendar');
const username = document.getElementById("username").value
const userTitle = document.getElementById("userTitle").value

function updateCalendarEvents() {
    // Fetch scheduled workouts from the backend
    fetch(`${url}/api/v1/schedule-workouts/calendar/${username}`, {
        headers: {
            "Accept": "application/json"
        }
    }).then(res => res.json())
        .then(data => {
            if (data && data.length > 0) {
                let calendarEvents = data.map(workout => {
                    const isClient = userTitle === 'CLIENT';
                    return {
                        id: workout.id,
                        name: isClient ? `Coach: ${workout.coachName}` : `Client: ${workout.clientName}`,
                        date: moment(workout.scheduledDateTime).format('MM-DD-YYYY'),
                        description: "Workout",
                        type: "event",
                        allowReschedule: true
                    };
                });


                // Use setCalendarEvents to update the calendar events
                $(calendar).evoCalendar({
                    onMonthChange: updateCalendarEvents,
                    onYearChange: updateCalendarEvents,
                    calendarEvents: calendarEvents,
                });

                $(calendar).on('selectEvent', function (event, activeEvent) {
                    var selectedIndex = activeEvent.id;

                    console.log("Selected Event Index: " + selectedIndex);

                    if (confirm("Do you want to delete this event?")) {
                        deleteCalendarEvent(selectedIndex);
                    }

                })
            } else {
                $('#calendar').evoCalendar({
                    theme: 'Default',
                    format: 'MM dd, yyyy',
                    titleFormat: 'MM yyyy',
                    eventHeaderFormat: 'MM dd, yyyy',
                    firstDayOfWeek: 0
                });
            }
        })
        .catch(error => console.error('Error fetching data:', error));
}

function deleteCalendarEvent(eventId) {
    fetch(`${url}/api/v1/schedule-workouts/calendar/${username}/${eventId}`, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        }
    }).then(res => {
        if (res.ok) {
            $(calendar).evoCalendar('removeCalendarEvent', eventId);
        } else {
            console.error('Failed to delete event: ', res.statusText);
        }
    }).catch(error => console.error('Error deleting event:', error));
}

updateCalendarEvents();





