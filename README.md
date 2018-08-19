# FlickrSample
Sample project to load images using Flickr API

# Introduction
Objective of the applictaion is to display the images based on the user search utilizing Flickr API. App consists of a single screen with a search bar. When the user searches for a keyword, result images were displayed, allowing the user to scroll through. Next set of images were loaded on reaching the list end.

# Approach
<b>Invoking API</b></br>
AsyncTask is used to manage the HTTP calls considering the restriction not to use libraries. Success and failure scenarios were handled gracefully using callbacks. This class is decoupled from the Activity and a singleton class is used to invoke the methods<br>
Made use of Picasso for loading images through URL. FetchImageAsync class is written to load the images and to map to the view. But, its not used because of a UI glitch when associated with recycler view.

<b>Parser</b></br>
Manual parser is written to map the response to the model.

<b>Utils</b></br>
All the generic methods were packaged under utils

# Scope for Improvements
We can extend the application in the below metioned aspects
<li> Applictaions orientation is fixed considering the challenges w.r.t Async Task. Orientation support can be added
<li> Can make use of Thread pool Executors and a suitable collection for fetching and caching images.
<li> We can go for MVP architecture considering modularity and testability
<li> UI can be beautified
<li> More test cases can be implemented. Due to time constraints, the only thing that is tested is NetworkUtils.
