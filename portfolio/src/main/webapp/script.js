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

function createMap() {
  
  var myLatlng = new google.maps.LatLng(13.7563, 100.5018);
  var mapOptions = {
    zoom: 2,
    center: myLatlng,
    mapTypeId: 'hybrid'
  };

  //photo locations 
  var cinqueTerre = {lat: 44.1461, lng: 9.6439};
  var googleMilan = {lat: 45.486183, lng: 9.189545};
  var watSaket = {lat: 13.7538, lng: 100.5066};
  var watPho = {lat: 13.7466, lng: 100.4933};
  var googleThailand = {lat: 13.742671, lng: 100.548082};
  var anantaraGoldenTriangle = {lat: 20.367355, lng: 100.077086};
  var malaMala = {lat: -24.800415, lng: 31.540408};
  var vAWaterfront = {lat: -33.903594, lng:18.420915};
  var lightHouse = {lat: -34.356695, lng:18.497123};
  //map
  var map = new google.maps.Map(document.getElementById('map'),
      mapOptions);

  //markers
  var watSaketMarker = new google.maps.Marker({position: watSaket, map: map});
  var cinqueTerreMarker = new google.maps.Marker({position: cinqueTerre, map: map});
  var googleMilanMarker = new google.maps.Marker({position: googleMilan, map: map});
  var watPhoMarker = new google.maps.Marker({position: watPho, map: map});
  var googleThailandMarker = new google.maps.Marker({position: googleThailand, map: map});
  var malaMalaMarker = new google.maps.Marker({position: malaMala, map: map});
  var anantaraGoldenTriangleMarker = new google.maps.Marker({position: anantaraGoldenTriangle, map: map});
  var vAWaterfrontMarker = new google.maps.Marker({position: vAWaterfront, map: map});
  var lightHouseMarker = new google.maps.Marker({position: lightHouse, map: map});

  //info strings
  var watSaketInfoString = '<h2>Wat Saket</h2>' +
      '<p>The Golden Mount is a sacred pilgrimage site during the week-long ' +
      'worshipping period in November. Getting to the top requires a climb ' +
      'up some 300 steps that encircle the chedi like a loosely coiled snake.' +
      'The well-paved path is relatively easy to tackle, especially if you visit ' +
      'early in the day or during mild climate in Bangkok. <br><br>' +
      'Wat Saket was the capital' + 's crematorium and the dumping ground ' +
      'for some 60,000 plague victims in the late-18th century. At the ' +
      'base of the Golden Mount, you’ll find an unusual cemetery covered ' +
      'in vines and overgrown trees. It emits a rather spooky out-of-era ' +
      'vibe. Once you arrive at the top of Wat Saket, you’ll be surrounded ' +
      'by a wall of bells and panoramas of Bangkok Old Town. <br><br>' +
      'Source: <a href="https://www.hotels.com/go/thailand/wat-saket">' +
      'https://www.hotels.com/go/thailand/wat-saket</a></p>';
  var cinqueTerreInfoString = '<h2>Cinque Terre</h2>' +
      '<p>The Cinque Terre, five towns, is a string of five fishing ' +
      'villages perched high on the Italian Riviera in the region Liguria, ' +
      'which until recently were linked only by mule tracks and accessible ' +
      'only by rail or water. <br><br>' +
      'An ancient system of footpaths is still the best way to visit the ' +
      'five villages: click here for Monterosso, Vernazza, Corniglia, ' +
      'Manarola and Riomaggiore. <br><br>' +
      'The Cinque Terre is noted for its beauty. Over centuries, people ' +
      'have carefully built terraces to cultivate grapes and olives on the ' +
      'rugged, steep landscape right up to the cliffs that overlook the ' +
      'Mediterranean Sea. <br><br>' +
      'Source: <a href="https://www.cinqueterre.eu.com/en/">https://www.cinqueterre.eu.com/en/</a></p>';
  var googleMilanInfoString = '<h2>Google Italy</h2>' +
      '<p>This is the location of the main Google office in Italy!</p>';
  var watPhoInfoString = '<h2>Wat Pho</h2>' +
      '<p>The highlight for most people visiting Wat Pho is the Reclining ' +
      'Buddha. The figures here are impressive: 15 meters tall, 46 meters ' +
      'long, so large that it feels like it’s been squeezed into the ' +
      'building. The Buddha' + 's feet are 5 meters long and exquisitely ' +
      'decorated in mother-of-pearl illustrations of auspicious laksanas ' +
      '(characteristics) of the Buddha. The number 108 is significant, ' +
      'referring to the 108 positive actions and symbols that helped lead ' +
      'Buddha to perfection. <br><br>' +
      'You’ll need to take your shoes off to enter, and if you’d like a ' +
      'little good luck, we recommend purchasing a bowl of coins at the ' +
      'entrance of the hall which you can drop in the 108 bronze bowls ' +
      'which line the length of the walls. Dropping the small pennies in ' +
      'makes a nice ringing sound and even if your wishes don’t come true, ' +
      'the money goes towards helping the monks renovate and preserve Wat Pho. ' +
      'As this is a revered image, all visitors must wear appropriate clothing ' +
      '– no exposed shoulders or skin above the knee.<br><br>' +
      'Source: <a href="https://www.hotels.com/go/thailand/wat-pho">https://www.hotels.com/go/thailand/wat-pho</a></p>';
  var googleThailandInfoString = '<h2>Google Thailand</h2>' +
      '<p>This is the location of the Google office in Thailand!</p>';
  var malaMalaInfoString = '<h2>Mala Mala Game Reserve</h2>' +
      '<p>MalaMala Game Reserve is the safari industry’s blueprint for the ' +
      'luxury photographic safari. In existence since 1927, this massive, ' +
      'thriving tract of land offers the most exciting wildlife experience ' +
      'this side of the equator. MalaMala Game Reserve is one of the largest ' +
      'private Big Five game reserves in South Africa. It covers 13 300ha ' +
      '(33 000 acres), shares a 19km (12 mile) unfenced boundary with the ' +
      'world-renowned Kruger National Park and lies strategically sandwiched ' +
      'between the Kruger and the Sabi Sand Reserve.<br><br>' +
      'Source: <a href="https://www.malamala.com/">https://www.malamala.com/</a></p>';
  var anantaraGoldenTriangleInfoString = '<h2>Anantara Golden Triangle</h2>' +
      '<p>Tucked into 160 acres of ancient jungle in northern Thailand, ' +
      'Anantara Golden Triangle Elephant Camp & Resort rests on a bluff ' +
      'overlooking the fabled convergence of two rivers and three ' +
      'countries. The region also boasts a rich cultural heritage, as seen ' +
      'in the many hill tribe peoples or at the Hall of Opium Museum. At ' +
      'Anantara Golden Triangle, the natural and cultural merge at our ' +
      'unique onsite Elephant Camp, modelled on traditional mahout villages ' +
      'and home to the resort’s very own gentle giants.<br><br>' +
      'Source: <a href="https://www.tripadvisor.com/Hotel_Review-g317131-d325842-Reviews-Anantara_Golden_Triangle_Elephant_Camp_Resort-Chiang_Saen_Chiang_Rai_Province.html">' +
      'https://www.tripadvisor.com/Hotel_Review-g317131-d325842-Reviews-Anantara_Golden_Triangle</a></p>';
  var vAWaterfrontInfoString = '<h2>V&A WaterFront</h2>' +
      '<p>The Victoria & Alfred (V&A) Waterfront in Cape Town is situated on ' +
      'the Atlantic shore, Table Bay Harbour, the City of Cape Town and Table ' +
      'Mountain. Adrian van der Vyver designed the complex.<br><br>' +
      'Situated in South Africa' + 's oldest working harbor, the 123 ' +
      'hectares (300 acres) area has been developed for mixed-use, with ' +
      'both residential and commercial real estate.<br><br>' +
      'The Waterfront attracts more than 23 million visitors a year.<br><br>' +
      'Source: <a href="https://en.wikipedia.org/wiki/Victoria_%26_Alfred_Waterfront">' +
      'https://en.wikipedia.org/wiki/Victoria_%26_Alfred_Waterfront</a></p>';
  var lightHouseInfoString = '<h2>New Cape Point Lighthouse</h2>' +
      '<p> Named the ‘Cape of Storms’ by Bartolomeu Dias in 1488; the ' +
      '‘Point’ was treated with respect by sailors for centuries. By day, ' +
      'it was a navigational landmark and by night, and in fog, it was a ' +
      'menace beset by violent storms and dangerous rocks that over the ' +
      'centuries littered shipwrecks around the coastline.<br><br>' +
      'In 1859 the first lighthouse was completed; it still stands at 238 ' +
      'metres above sea-level on the highest section of the peak and is now ' +
      'used as the centralised monitoring point for all the lighthouses on ' +
      'the coast of South Africa. Access to this historical building is by ' +
      'an exhilarating three-minute ride in the wheelchair-accessible Flying ' +
      'Dutchman funicular that transfers visitors from the lower station at ' +
      '127 metres above sea-level, to the upper station.<br><br>' +
      'Source: <a href="https://capepoint.co.za/about/">https://capepoint.co.za/about/</a></p>';

  //info windows
  var watSaketInfowindow = new google.maps.InfoWindow({
    content: watSaketInfoString
  });
  var cinqueTerreInfowindow = new google.maps.InfoWindow({
    content: cinqueTerreInfoString
  });
  var googleMilanInfowindow = new google.maps.InfoWindow({
    content: googleMilanInfoString
  });
  var watPhoInfowindow = new google.maps.InfoWindow({
    content: watPhoInfoString
  });
  var googleThailandInfowindow = new google.maps.InfoWindow({
    content: googleThailandInfoString
  });
  var malaMalaInfowindow = new google.maps.InfoWindow({
    content: malaMalaInfoString
  });
  var anantaraGoldenTriangleInfowindow = new google.maps.InfoWindow({
    content: anantaraGoldenTriangleInfoString
  });
  var vAWaterfrontInfowindow = new google.maps.InfoWindow({
    content: vAWaterfrontInfoString
  });
  var lightHouseInfowindow = new google.maps.InfoWindow({
    content: lightHouseInfoString
  });

  //adding info windows
  watSaketMarker.addListener('click', function() {
    watSaketInfowindow.open(map, watSaketMarker);
  });
  cinqueTerreMarker.addListener('click', function() {
    cinqueTerreInfowindow.open(map, cinqueTerreMarker);
  });
  googleMilanMarker.addListener('click', function() {
    googleMilanInfowindow.open(map, googleMilanMarker);
  });
  watPhoMarker.addListener('click', function() {
    watPhoInfowindow.open(map, watPhoMarker);
  });
  googleThailandMarker.addListener('click', function() {
    googleThailandInfowindow.open(map, googleThailandMarker);
  });
  malaMalaMarker.addListener('click', function() {
    malaMalaInfowindow.open(map, malaMalaMarker);
  });
  anantaraGoldenTriangleMarker.addListener('click', function() {
    anantaraGoldenTriangleInfowindow.open(map, anantaraGoldenTriangleMarker);
  });
  vAWaterfrontMarker.addListener('click', function() {
    vAWaterfrontInfowindow.open(map, vAWaterfrontMarker);
  });
  lightHouseMarker.addListener('click', function() {
    lightHouseInfowindow.open(map, lightHouseMarker);
  });

}

function onLoad() {
  createMap();
  rotateImages();
}

