package com.zw.zwbase.ui

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.LocalImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.zw.zwbase.R

@Composable
fun ZWAsyncImage(
    modifier: Modifier = Modifier,
    dataPhoto: Any,
    model: Any? = ImageRequest.Builder(LocalContext.current)
        .data(dataPhoto)
        .placeholder(R.drawable.placeholder_gray)
        .error(R.drawable.placeholder_white)
        .crossfade(true)
        .build(),
    contentDescription: String? = null,
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) = AsyncImage(
    model = model,
    contentDescription = contentDescription,
    imageLoader = LocalImageLoader.current,
    modifier = modifier,
    transform = transform,
    onState = onState,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter,
    filterQuality = filterQuality
)

@Composable
fun ZWGifImage(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).components {
        if (Build.VERSION.SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = drawableRes)
                .apply(block = {
                    size(Size.ORIGINAL)
                }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
        contentScale = contentScale
    )
}