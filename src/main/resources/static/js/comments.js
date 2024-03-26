const url = 'http://localhost:8080'

const workoutId = document.getElementById('workoutId').value
const commentForm = document.getElementById('commentForm')
commentForm.addEventListener('submit', postComment)

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

const commentContainer = document.getElementById('commentCtnr');
const showCommentsBtn = document.getElementById('showComments');


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

function commentAsHTML(comment) {
    let commentHTML = `<div id="${comment.id}">\n`
    commentHTML += `<h4>${comment.authorUsername}</h4>\n`
    commentHTML += `<p>${comment.message}</p>\n`
    commentHTML += `<button class="btn btn-danger" onclick="deleteComment(${comment.id})">Delete</button>\n`
    commentHTML += '</div>\n'

    return commentHTML;
}

function deleteComment(commentId) {
    fetch(`${url}/api/v1/${programId}/${weekId}/${workoutId}/comments/${commentId}`, {
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