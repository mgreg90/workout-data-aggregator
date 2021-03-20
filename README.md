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
npm install
```

#### Run the development server
```sh
npm run serve
```

#### Deploy the Android App
Follow [this guide from the Ionic Framework documentation](https://ionicframework.com/docs/developing/android)

After making significant changes, you'll need to reload the android code.
To do so, run:
```sh
ionic capacitor copy android
```

### Server Development

#### MongoDB
To install (on Mac):
```sh
brew install mongodb-community@4.4
```

To start:
```sh
brew services start mongodb-community
```

To stop:
```sh
brew services stop mongodb-community
```
