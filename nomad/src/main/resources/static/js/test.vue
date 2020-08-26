<template>
    <div>
        <h1>Hello {{test}}</h1>
    </div>
</template>

<script>
    module.exports =
    {
        data()
        {
            return {test: "My man"}
        }
    };
</script>
<style>
    h1
    {
        color: #f00;
    }
</style>
