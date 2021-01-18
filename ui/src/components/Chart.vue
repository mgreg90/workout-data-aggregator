<template>
<canvas id="myChart"></canvas>
</template>

<script>
import Chart from 'chart.js'

export default {
  mounted() {
    const canvas = document.getElementById("myChart").getContext('2d');
    const myChart = new Chart(canvas, {
      type: 'line',
      data: {
        labels: ["Jan-1", "Jan-2", "Jan-3", "Jan-4"],
        datasets: [
          {
            label: "Pushups",
            data: [60, 52, 71, 75],
            borderColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(255, 99, 132, 0.2)',
              'rgba(255, 99, 132, 0.2)',
              'rgba(255, 99, 132, 0.2)'
            ]
          },
          {
            label: "Pullups",
            data: [19, 27, 23, 31],
            borderColor: [
              'rgba(0, 162, 235, 0.2)',
              'rgba(0, 162, 235, 0.2)',
              'rgba(0, 162, 235, 0.2)',
              'rgba(0, 162, 235, 0.2)'
            ]
          }
        ]
      },
      options: {
        scales: {
          yAxes: [
            {
              ticks: {
                beginAtZero: true,
                max: 100
              }
            }
          ]
        }
      }
    })
  }
};
</script>
