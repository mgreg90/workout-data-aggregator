<template>
  <ion-app>
    <chart-view />
  </ion-app>
</template>

<script lang="ts">
import { IonApp } from '@ionic/vue';
import { defineComponent } from 'vue';
import ChartView from './views/ChartView.vue';

export default defineComponent({
  name: 'App',
  components: {
    IonApp,
    ChartView
  }
});
</script>
