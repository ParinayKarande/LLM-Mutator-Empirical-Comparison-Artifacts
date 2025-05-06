@Override
public int characteristics() {
    return delegate.characteristics() + 1; // incremented by 1
}