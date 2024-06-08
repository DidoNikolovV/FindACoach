const url = 'http://localhost:8080'

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

const currentLocation = window.location.href.toString();
const commentContainer = document.getElementById('commentCtnr');

if(currentLocation.startsWith('http://localhost:8080/forum/topic')) {
    const topicCommentForm = document.getElementById("topicCommentForm");
    topicCommentForm.addEventListener("submit", postTopicComment);
} else {
    const workoutId = document.getElementById('workoutId').value
    const commentForm = document.getElementById('commentForm')
    commentForm.addEventListener('submit', postComment)
    const showCommentsBtn = document.getElementById('showComments');
}

async function postComment(e) {
    e.preventDefault();

    const messageValue = document.getElementById('message').value;

    fetch(`${url}/api/v1/comments/${workoutId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify({
            message: messageValue
        })
    }).then(res => res.json())
        .then(data => {
            document.getElementById('message').value = ""
            commentContainer.innerHTML += commentAsHTML(data)
        })
}

function postTopicComment(e) {
    e.preventDefault();

    const messageValue = document.getElementById('message').value;
    const topicId = document.getElementById("topicId").value;

    fetch(`${url}/api/v1/comments/${topicId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify({
            message: messageValue
        })
    }).then(res => res.json())
        .then(data => {
            document.getElementById('message').value = ""
            commentContainer.innerHTML += commentAsHTML(data)
        })
}

function commentAsHTML(comment) {
    let commentHTML = `<div id="${comment.id}" class="d-flex align-items-center mb-3">\n`;
    commentHTML += `<img src="${comment.profilePicture}" class="rounded-circle mr-3 mb-4" alt="Profile Picture" style="width: 40px; height: 40px;">\n`;
    commentHTML += `<div>\n`;
    commentHTML += `<h4 class="mb-0">${comment.authorUsername}</h4>\n`;
    commentHTML += `<p>${comment.message}</p>\n`;
    commentHTML += `<button class="btn btn-danger" onclick="deleteComment(${comment.id})">Delete</button>\n`;
    commentHTML += `</div>\n`;
    commentHTML += `</div>\n`;

    return commentHTML;
}


function deleteComment(commentId) {
    fetch(`${url}/api/v1/comments/${workoutId}/${commentId}`, {
        method: 'DELETE',
        headers: {
            [csrfHeaderName]: csrfHeaderValue
        }
    }).then(res => res.json())
        .then(data => {
            const commentId = data.id;
            document.getElementById(commentId).remove();
        })
}

function loadComments() {
    fetch(`${url}/api/v1/comments/${workoutId}/all`, {
        headers: {
            "Accept": "application/json"
        }
    }).then(res => res.json())
        .then(data => {
            for (let comment of data) {
                commentContainer.innerHTML += commentAsHTML(comment)
            }
        })
}

function loadTopicComments() {
    fetch(`${url}/api/v1/comments/topic/all`, {
        headers: {
            "Accept": "application/json"
        }
    }).then(res => res.json())
        .then(data => {
            for (let comment of data) {
                commentContainer.innerHTML += commentAsHTML(comment)
            }
        })
}

loadComments();
loadTopicComments();