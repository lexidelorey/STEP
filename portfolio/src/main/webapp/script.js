// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
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
