# 列表模块 - RecyclerView

本模块基于 `MultiType` 封装，简化业务层对于列表的实现，尤其是对多类型的列表

### 使用

```java
CheetahApdapter adapter = new CheetahAdapter();

// 注册列表要处理的数据类型，以及该数据对应的ViewBinder
adapter.register(SampleItem.class, new SampleItemViewBinder());

// 多种数据类型直接多次register，会自动处理多type的情况
adapter.register(AnotherItem.class, new AnotherItemViewBinder());

// 支持数据变化比较，只修改变化数据的UI，减少多余的重绘
adapter.enableDiff(new SampleDiffCallback());

// 设置数据
adapter.setItems(results);

// 通知UI改变
adapter.notifyDataSetChanged();

```

**关于 adapter 该如何增加修改数据的问题，网上也有很多讨论，这一点上我与`MultiType`作者的观点相同，adapter作为数据的展示类，不应该过多去关心数据的修改，而是应该由业务层对应的数据持有类来进行操作，然后通知adapter进行修改。例如，mvp架构中，应该由P层持有数据，在数据改变后通知View层去修改数据，保证每个层的纯粹性和数据的一致性**

- AbsItemViewBinder

该类为处理对应数据类型的 `ViewBinder` ，`Adapter` 通过不同的数据找到不同的`ViewBinder` 来实现UI的渲染以及逻辑的处理

构造一个 `ItemViewBinder` 继承`AbsItemViewBinder` ，泛型种为要处理的数据，及对应的`ViewHolder`

实际上这个类的结构就是一个小型的 MVP ，数据为Model层，ViewHolder 为View层，而ItemViewBinder就是就presenter，实现数据和UI的双向绑定

```java
public class ScanViewBinder extends AbsItemViewBinder<ScanResult, ScanViewBinder.ScanViewHolder>{
    @Override
    protected int layout() {
        return R.layout.item_scan;
    }

    @Override
    protected ScanViewHolder createViewHolder(View itemView) {
        return new ScanViewHolder(itemView);
    }

    @Override
    protected void onBind(ScanViewHolder holder, ScanResult item) {
        holder.line1.setText(item.getName());
    }


    static class ScanViewHolder extends AbsViewHolder<ScanResult> {

        TextView line1;
        public ScanViewHolder(View itemView) {
            super(itemView);
            line1 = findViewById(R.id.scan_line1);
        }
    }
}
```

