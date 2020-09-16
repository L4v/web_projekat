<template>
    <!-- TODO(Jovan): Use floating-label -->
    <div>
        <input type="text" placeholder="Search...">
        <div id="result-container">
            <ul id="result-list">
                <li class="search-result" v-for="result in searchResults">{{result}}</li>
            </ul>
        </div>
    </div>
</template>

<script>
    module.exports = {
        props: ["searchData", "searchQuery"],
        data: function()
        {
            return {
                searchResults: [],
            }
        },
        methods:
        {

        }
    }
</script>