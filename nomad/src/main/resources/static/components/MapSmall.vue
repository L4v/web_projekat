<template>
    <div id="map-container">
    </div>
</template>

<script>
    module.exports =
    {
        props:
        {
            lat:
            {
                default: 40.7259,
            },
            lon:
            {
                default: -73.9805,
            },
            zoom:
            {
                default: 12,
            },
            setmarker:
            {
                default: false,
            },
        },
        mounted()
        {
            var map = L.map('map-container').setView([this.lat, this.lon], this.zoom);
            L.tileLayer('https://{s}-tiles.locationiq.com/v2/obk/r/{z}/{x}/{y}.png?key=' + locationiq.key).addTo(map);
            var geocoder = L.control.geocoder(locationiq.key);
            geocoder.addTo(map);
            geocoder.on("select", e => 
            {
                // console.log("RESULTS " + JSON.stringify(e.feature.feature));
                this.$emit("search", e.feature.feature);
            });

            if(this.marker)
            {
                // TODO(Jovan): Fix not working
                L.marker([this.lat, this.lon]).addTo(map);
            }
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