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
images[3] = "./Images/GoldenCorner.jpg";
images[4] = "./Images/GoldPalace.jpg";
images[5] = "./Images/GoogleThailand.jpg";
images[6] = "./Images/ItalianPizza.jpg";
images[7] = "./Images/LayingBuddah.jpg";
images[8] = "./Images/TowerOfPisa.jpg";
images[9] = "./Images/Venice.jpg";
images[10] = "./Images/Zipline.jpg";
images[11] = "./Images/BackWalk.jpg";
images[12] = "./Images/Bridge.jpg";
images[13] = "./Images/Ele.jpg";
images[14] = "./Images/MeerSmi.jpg";
images[15] = "./Images/EleFeed1.jpg";
images[16] = "./Images/EleSunset.jpg";
images[17] = "./Images/EleWash.jpg";
images[18] = "./Images/Table.jpg";
images[19] = "./Images/Giraffe.jpg";
images[20] = "./Images/Sunset.jpg";

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

function getCommentsUsingArrows() {
  fetch('/data').then(response => response.json()).then((comments) => {
    commentsList = document.getElementById('comment-container');
    commentsList.innterHTML = '';
    commentsList.appendChild(
      createListElement(comments[0]));
    commentsList.appendChild(
      createListElement(comments[1]));
    commentsList.appendChild(
      createListElement(comments[2]));
    commentsList.appendChild(
      createListElement(comments[3]));
  });
}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
