<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>Fitness Data</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content :fullscreen="true">
      <ion-header collapse="condense">
        <ion-toolbar>
          <ion-title size="large">Fitness Data</ion-title>
        </ion-toolbar>
      </ion-header>
    </ion-content>
    <chart />
  </ion-page>
</template>

<script lang="ts">
import { camera, trash, close } from 'ionicons/icons'
import { actionSheetController, IonPage, IonHeader, IonToolbar, IonTitle,
         IonContent } from '@ionic/vue'
import { usePhotoGallery, Photo } from '@/composables/usePhotoGallery'
import Chart from '@/components/Chart.vue'

export default  {
  name: 'ChartView',
  components: { IonHeader, IonToolbar, IonTitle, IonContent, IonPage, Chart },
  setup() {
    const { photos, takePhoto, deletePhoto } = usePhotoGallery();

    const showActionSheet = async (photo: Photo) => {
      const actionSheet = await actionSheetController.create({
        header: 'Photos',
        buttons: [{
          text: 'Delete',
          role: 'destructive',
          icon: trash,
          handler: () => {
            deletePhoto(photo);
        }}, {
          text: 'Cancel',
          icon: close,
          role: 'cancel',
          handler: () => {
            // Nothing to do, action sheet is automatically closed
          }
        }]
      });
      await actionSheet.present();
    }

    return {
      photos,
      takePhoto,
      showActionSheet,
      camera, trash, close
    }
  }
}
</script>
