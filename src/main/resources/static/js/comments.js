const url = 'http://localhost:8080'

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content

const currentLocation = window.location.href.toString();
const commentContainer = document.getElementById('commentCtnr');
let workoutId = "";
if(currentLocation.startsWith('http://localhost:8080/forum/topic')) {
    const topicCommentForm = document.getElementById("topicCommentForm");
    topicCommentForm.addEventListener("submit", postTopicComment);
} else {
    workoutId = document.getElementById('workoutId').value
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

    fetch(`${url}/api/v1/comments/topic/${topicId}`, {
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
    let profilePicture = comment.profilePicture ? comment.profilePicture : '/images/profile-avatar.jpg';
    let commentHTML = `<div id="${comment.id}" class="card comment-card mb-3">\n`;
    commentHTML += `<div class="card-header d-flex align-items-center">\n`;
    commentHTML += `<img src="${profilePicture}" class="rounded-circle mr-3" alt="Profile Picture" style="width: 40px; height: 40px;">\n`;
    commentHTML += `<small class="text-muted">${comment.authorUsername}</small>\n`;
    commentHTML += `</div>\n`;
    commentHTML += `<div class="card-body">\n`;
    commentHTML += `<p class="text-muted">${comment.message}</p>\n`;
    commentHTML += `<div class="d-flex justify-content-start mt-2">\n`;
    commentHTML += `<span id="like-count-comment1">${comment.likes} Likes</span>\n`;
    commentHTML += `<button class="btn btn-sm mr-2 like-btn border-0" onclick="likeComment(${comment.id})">Like</button>\n`;
    commentHTML += `<button class="btn btn-sm delete-btn border-0" onclick="deleteComment(${comment.id})">Delete</button>\n`;
    commentHTML += `</div>\n`;
    commentHTML += `<div class="reply-form" style="display: none; margin-top: 10px;">\n`;
    commentHTML += `<textarea class="form-control mb-2" rows="2" placeholder="Write a reply..."></textarea>\n`;
    commentHTML += `<button class="btn btn-sm btn-primary submit-reply-btn" type="button">Submit</button>\n`;
    commentHTML += `</div>\n`;
    commentHTML += `</div>\n`;
    commentHTML += `<div class="replies-container pl-4"></div>\n`;
    commentHTML += `</div>\n`;

    return commentHTML;
}


function deleteComment(commentId) {
    fetch(`${url}/api/v1/comments/${workoutId}/${commentId}`, {
        method: 'DELETE',
        headers: {
            [csrfHeaderName]: csrfHeaderValue
        }
    })
        .then(res => {
            if (res.ok) {
                const commentElement = document.getElementById(commentId);
                console.log("Deleting comment with ID:", commentId);
                if (commentElement) {
                    commentElement.remove();
                } else {
                    console.error("Comment element not found in DOM:", commentId);
                }
            } else {
                return res.json().then(data => {
                    console.error("Failed to delete comment:", data);
                });
            }
        })
        .catch(error => {
            console.error("Error deleting comment:", error);
        });
}

function likeComment(commentId) {
    fetch(`${url}/api/v1/comments/${workoutId}/${commentId}/like`, {
        method: 'POST',
        headers: {
            [csrfHeaderName]: csrfHeaderValue,
            'Content-Type': 'application/json'
        }
    })
        .then(res => {
            if (res.ok) {
                console.log("Successfully liked comment with ID:", commentId);
                // Optionally, update the UI to reflect the like status
                const likeButton = document.querySelector(`button[onclick="likeComment(${commentId})"]`);
                if (likeButton) {
                    // Update the button or some element to show the liked status
                    likeButton.textContent = "Liked"; // Example update
                } else {
                    console.error("Like button element not found in DOM:", commentId);
                }
            } else {
                return res.json().then(data => {
                    console.error("Failed to like comment:", data);
                });
            }
        })
        .catch(error => {
            console.error("Error liking comment:", error);
        });
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

let currentPage = 1;
const commentsPerPage = 4;

function loadTopicComments(currentPage = 1) {
    const topicId = document.getElementById("topicId").value;
    const urlWithParams = `${url}/api/v1/comments/topic/${topicId}/all?page=${currentPage - 1}&size=${commentsPerPage}`;

    fetch(urlWithParams, {
        headers: {
            "Accept": "application/json"
        }
    })
        .then(res => res.json())
        .then(data => {
            renderComments(data.content);
            renderPaginationControls(data.totalPages, currentPage);
        });
}

function renderComments(comments) {
    const commentContainer = document.getElementById('commentCtnr');
    commentContainer.innerHTML = '';
    comments.forEach(comment => {
        commentContainer.innerHTML += commentAsHTML(comment);
    });
}

function renderPaginationControls(totalPages, currentPage) {
    const paginationControls = document.getElementById('paginationControls');
    paginationControls.innerHTML = '';

    for (let i = 1; i <= totalPages; i++) {
        const pageItem = document.createElement('li');
        pageItem.className = `page-item ${i === currentPage ? 'active' : ''}`;
        pageItem.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        pageItem.addEventListener('click', function(event) {
            event.preventDefault();
            loadTopicComments(i);
        });
        paginationControls.appendChild(pageItem);
    }
}

loadComments();
loadTopicComments();