/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

/* Avoid adding ID selector rules in this style sheet, since they could
 * inadvertently match elements in the article content. */

browser.browserAction.onClicked.addListener(() => {
  const exampleUrl = "https://www.example.com/";

  browser.tabs.query({url: exampleUrl}, (tabs) => {
    if (tabs.length > 0) {
      // Ein Tab mit der URL example.com ist bereits geöffnet,
      // also wechseln Sie zu diesem Tab.
      browser.tabs.update(tabs[0].id, {active: true});
    } else {
      // Kein Tab mit der URL example.com ist geöffnet,
      // also öffnen Sie einen neuen Tab.
      browser.tabs.create({url: exampleUrl});
    }
  });
});

browser.browserAction.setBadgeBackgroundColor({color: "#AAAAAA"});