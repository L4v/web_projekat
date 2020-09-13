<template>
    <div id="map-container">
    </div>
</template>

<script>
    module.exports =
    {
        mounted()
        {
            var map = L.map('map-container').setView([40.7259, -73.9805], 12);
            L.tileLayer('https://{s}-tiles.locationiq.com/v2/obk/r/{z}/{x}/{y}.png?key=' + locationiq.key).addTo(map);
            var geocoder = L.control.geocoder(locationiq.key);
            geocoder.addTo(map);
            geocoder.on("select", e => 
            {
                // console.log("RESULTS " + JSON.stringify(e.feature.feature));
                this.$emit("search", e.feature.feature);
            });
        }
    }
</script>

<style scoped>
    #map-container
    {
        width: 100%;
        height: 400px;
    }
</style>