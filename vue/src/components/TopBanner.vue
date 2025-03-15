<template>
    <div class="top-banner">
        <i class="fas fa-utensils icon-left"></i>
        <h1 @click="$router.push({name: 'home'})">Kitchen Jam</h1>
        <i class="fas fa-music icon-right"></i>
    </div>
</template>
<script>
export default {
    props: ['animate'],
    mounted() {
        if (this.animate) {
            setTimeout(() => {
                const icons = document.querySelectorAll('.icon-left, .icon-right');
                icons.forEach((icon, index) => {
                    setTimeout(() => {
                        icon.classList.add('visible');
                    }, index * 500); // Stagger the animation by 500ms
                });
            }, 2000); // Wait 2 seconds before starting the animation
        } else {
            const icons = document.querySelectorAll('.icon-left, .icon-right');
            icons.forEach(icon => {
                icon.classList.add('visible');
            });
        }
    }
}
</script>
<style scoped>
h1 {
    margin-left: 15px;
    margin-right: 15px;
}
.top-banner {
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
}
.top-banner:hover {
    cursor: pointer;
}
.icon-left, .icon-right {
    font-size: 24px;
    opacity: 0;
    transform: translateY(100%);
    transition: transform 0.5s cubic-bezier(0.68, -0.55, 0.27, 1.55), opacity 0.5s ease-in;
}

.icon-left {
    margin-right: 10px;
}

.icon-right {
    margin-left: 10px;
}

.icon-left.visible, .icon-right.visible {
    opacity: 1;
    transform: translateY(0);
}
</style>