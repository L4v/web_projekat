<template>
    <div>
        <h1>Comments</h1>
        <div id="comments">
            <div v-for="comment in comments" class="comment-container">
                <div class="comment-info">
                    <div><b>By:</b>{{comment.comment.guestId}}</div>
                    <div><b>Apartment:</b>{{comment.apartment.location.address.street}} {{comment.apartment.location.address.streetNumber}} {{comment.apartment.location.address.area}}</div>
                    <div><b>Rating:</b>{{comment}}</div>
                </div>
            </div>
        </div>
    </div>
</template>