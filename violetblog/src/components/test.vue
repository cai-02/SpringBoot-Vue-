<template>
  <div class="parent-div">
    <div class="triangle-div left"></div>
    <div class="triangle-div right"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {

    };
  },
  mounted() {

  },

}
</script>

<style>
.parent-div {
  width: 300px;
  height: 200px;
  position: relative;

  .triangle-div {
    width: 50%;
    height: 100%;
    position: absolute;
    top: 0;
  }

  .left {
    left: 0;
    clip-path: polygon(0 0, 100% 0, 0 100%);
    background-color: aqua;
  }

  .right {
    right: 0;
    left: 0;
    clip-path: polygon(100% 0, 0 100%, 100% 100%);
    background-color: aquamarine;
  }
}
</style>