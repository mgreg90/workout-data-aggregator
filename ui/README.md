Workout Data Aggregator
-------
## Resources
* [Google Drive](https://drive.google.com/drive/u/0/folders/1SfbMsvzlZq8DGRNFSgY_E43LxN-d12LK)
* [Kanban Board](https://app.asana.com/0/1199533333961783/board)

## Development
Clone the app
```sh
git clone git@github.com:mgreg90/workout-data-aggregator.git
```

### UI Development
#### Install dependencies
```sh
npm install -g @ionic/cli
npm install
```

#### Run the development server
```sh
ionic serve
```

#### Deploy the Android App For Development
Follow [this guide from the Ionic Framework documentation](https://ionicframework.com/docs/developing/android)

Once set up,
1) Open Android Studio
2) Press cmd+shift+a and type AVD Manager
3) Launch an emulator
4) Click Run (green arrow)

After making significant changes in your code, you will need to reload the web
code in your android app. To do so, run
```sh
ionic capacitor copy android
```
