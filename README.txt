//添加渲染(1.1.3+的变化)
RenderJSEvents.AddGuiRender(event => {
    event.addRender(context=>{
        RenderJSRenderSystem.setShaderColorJS(1,1,1,1)//重置颜色
        RenderJSRenderSystem.setShaderTextureJS(new ResourceLocation("minecraft:textures/gui/icons.png"))//设置资源位置,这里使用了原版的"textures/gui/icons.png"
        var width = context.window.guiScaledWidth/2//x轴缩放中心
        var height = context.window.guiScaledHeight/2//y轴缩放中心
        RenderJSGUI.blitJS(context.poseStack,width,height,53,0,9,9)//在setShaderTextureJS里设置的材质中从(53,0)开始向右和向下截取9个像素
    })
})

//注册ItemDecorations(1.1.3+的变化)
RenderJSEvents.RegisterItemDecorations(event => {
   //注册一个ItemDecorator,如果之前已经注册则返回之前注册的ItemDecorator,reload时会自动将新内容更新到对应的ItemDecorator 
    event.register("test:test1", "example", context => {
        var itemStack = context.itemStack
        var charge = 0
        if (itemStack.getOrCreateTag().contains("Charge")) {
            charge = itemStack.nbt.getInt("Charge")
        }

        //设置满充能为10
        charge = Math.min(charge, 10)
        //重置颜色
        RenderJSRenderSystem.setShaderColorJS(1, 1, 1, 1)
        //禁用深度测试 要不然贴图会在物品下面
        RenderJSRenderSystem.disableDepthTestJS()
        //纯色填充(int pX, int pY, int pWidth, int pHeight, int pRed, int pGreen, int pBlue, int pAlpha)
        RenderJSUtils.fillRect(context.xOffset + 2, context.yOffset + 12, 13, 1, 0, 0, 0, 255)//底色


        RenderJSUtils.fillRect(context.xOffset + 2, context.yOffset + 12, 13 * charge / 10, 1, 255, 255, 0, 255)//条色
    })
})