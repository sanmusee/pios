<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>{{ $route.meta.title }}</el-breadcrumb-item>
  </el-breadcrumb>
</template>
