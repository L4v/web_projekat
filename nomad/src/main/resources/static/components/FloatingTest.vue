<template>
    <div>
        <floating-label :inputdata.sync="parentData" type="text" placeholder="Test" name="Test"></floating-label>
        <p>Current content state: {{parentData}}</p>
        <input type="text" v-model="parentData"/>
    </div>
</template>

<script>
    module.exports = 
    {
        data: function()
        {
            return{
                parentData: "Some content",
            }
        }
    }
</script>
