function addFortune() {
  const fortunes =
      ['You will meet someone of importance to your future today', 
            'Make sure to speak your truth today.', 
            'The planets are totally aligned against you today', 
            'You look very pretty today :)', 
            'Your lucky word today is "watermelon"', 
            'The word of the day is "patience"'];

  // Pick a random greeting.
  const fortune = fortunes[Math.floor(Math.random() * fortunes.length)];

  // Add it to the page.
  const fortuneContainer = document.getElementById('fortune-container');
  fortuneContainer.innerText = fortune;
}

var i = 0;
var images = [];
var time = 3000;

images[0] = "./Images/BangkokOverlook.jpg";
images[1] = "./Images/Water.jpg";
images[2] = "./Images/CinqueTerre.jpg";
images[3] = "./Images/GoogleThailand.jpg";
images[4] = "./Images/LayingBuddah.jpg";
images[5] = "./Images/Venice.jpg";
images[6] = "./Images/GoogleMilan.jpg";
images[7] = "./Images/BackWalk.jpg";
images[8] = "./Images/EleFeed1.jpg";
images[9] = "./Images/EleSunset.jpg";
images[10] = "./Images/EleWash.jpg";
images[11] = "./Images/Table.jpg";
images[12] = "./Images/Giraffe.jpg";

function rotateImages() {
  document.slide.src = images[i];
  
  if (i < images.length - 1) {
    ++i;
  } 
  else {
    i = 0;
  }
  
  setTimeout("rotateImages()", time);
}

function getComments() {
  var value = document.getElementById('number-comments').value
  fetch('/data') 
    .then(response => response.json())
    .then((comments) => {
      commentsList = document.getElementById('comment-container');
      commentsList.innerHTML = '';
      var i;
      for (i = 0; i < value; i++) {
        commentsList.appendChild(createCommentElement(comments[i]));
      }
  });
}

function createCommentElement(comment) {
  const commentElement = document.createElement('div');
  commentElement.className = 'commentElement';

  const userName = document.createElement('h3');
  userName.id = 'userName';
  userName.innerText = comment.name;

  const dateTime = document.createElement('p');
  dateTime.id = 'dateTime';
  dateTime.innerText = comment.dateTime

  const commentBody = document.createElement('p');
  commentBody.id = 'commentBody';
  commentBody.innerText = comment.comment;

  const bottomOfComment = document.createElement('div');
  bottomOfComment.id = 'bottom-of-comment';

  const deleteButton = document.createElement('button');
  deleteButton.className = 'button comment-button';
  const deleteIcon = document.createElement('i');
  deleteIcon.className = 'fa fa-eraser';
  deleteButton.appendChild(deleteIcon);
  deleteButton.addEventListener('click', () => {
    deleteSingleComment(comment);
    commentElement.remove();
  });

  commentElement.appendChild(userName);
  commentElement.appendChild(dateTime);
  commentElement.appendChild(commentBody);
  commentElement.appendChild(bottomOfComment);
  commentElement.appendChild(deleteButton);
  return commentElement;
}

function deleteAllComments() {
    const request = new Request('/delete-all-comments', {method: 'POST'});
    fetch(request)
      .then(getComments());
} 

function deleteSingleComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-single-comment', {method: 'POST', body: params})
    .then(getComments());
}

