<template>
    <div id="slider-main">
        <a class="prev" href="#" @click="prev">&#10094;</a>
        <transition-group name="fade" tag="div">
            <div v-for="i in [currentIndex]" :key="i">
                <img :src="'rest/image?filename=' + currentImg">
            </div>
        </transition-group>
        <a class="next" href="#" @click="next">&#10095;</a>
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                currentIndex: 0,
            }
        },

        props: ["images"],

        methods:
        {
            next: function()
            {
                ++this.currentIndex;
            },


            prev: function()
            {
                --this.currentIndex;
            }
        },

        computed:
        {
            currentImg: function()
            {
                return this.images[Math.abs(this.currentIndex) % this.images.length];
            },
        },
    }
</script>

<style scoped>
    #slider-main
    {
        position: relative;
    }

    .fade-enter-active,
    .fade-leave-active
    {
        transition: all 0.9s ease;
        overflow: hidden;
        visibility: visible;
        position: absolute;
        width: 100%;
        opacity: 1;
    }

    .fade-enter,
    .fade-leave-to
    {
        width: 100%;
        opacity: 0;
    }

    img
    {
        height: 600px;
        width: 100%;
    }

    .next
    {
        right: 0;
    }

    .prev
    {
    }

    .next,
    .prev
    {
        /*cursor: pointer;
        position: absolute;
        top: 37.5%;
        width: auto;
        padding: 16px;
        color: white;
        font-weight: 500;
        font-size: 18px;
        transition: 0.7s ease;
        border-radius: 0 4px 4px 0;
        text-decoration: none;
        user-select: none;*/
        cursor: pointer;
        position: absolute;
        top: 50%;
        width: auto;
        margin-top: -22px;
        padding: 16px;
        color: white;
        font-weight: bold;
        font-size: 18px;
        transition: 0.6s ease;
        border-radius: 0 3px 3px 0;
        user-select: none;
    }

    .next:hover,
    .prev:hover
    {
        background-color: rgba(0, 0, 0, 0.9);
        color: #ff5722;
    }
</style>