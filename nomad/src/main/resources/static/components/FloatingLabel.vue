<template>
    <!-- NOTE(Jovan):
        Input field with floating placeholders
        
        options:
            placeholder - placeholder text
            name        - defines input name
            type        - defines input type (password or text)
    -->
    <!-- TODO(Jovan): Fix floating label not detecting input -->
    <div class="loginField floating-label">
        <label for="username">{{placeholder}}</label>
        <input 
            v-bind:name="name"
            v-bind:type="type"
            v-bind:value="value"
            v-on:input="$emit('input', $event.target.value)"
            @blur="$event.target.parentElement.classList.toggle('focused')"
            @focus="$event.target.parentElement.classList.toggle('focused')"
            />
    </div>
</template>
<script>
    module.exports = {
        props: ["placeholder", "name", "type", "value"]
    }
</script>
<style scoped>
    .floating-label
    {
        position: relative;
        margin-bottom: 10px;
    }

    .floating-label label
    {
        pointer-events: none;
        position: absolute;
        white-space: nowrap;
        overflow: hidden;
        top: 0;
        display: inline-block;
        background: #fff;
        margin: 8px 5px;
        padding: 0 10px;
        transition: position .2s linear;
        opacity: 0.6;
    }

    .focused label
    {
        transition: all .2s linear;
        opacity: 1;
        color: #ff5722;
        top: -20px;
    }
</style>
