package com.umar.apps.list;

import java.util.LinkedList;
import java.util.List;

/**
 * Please see <a href="https://stackoverflow.com/questions/54004821/is-t-list-extends-t-f-a-useful-signature">link</a>
 * for details.
 *
 * Question: Is <code><T> List<? extends T> f() </code> a useful signature?
 *
 * Answer:
 * ** You know that all elements of this list conform to the type T,
 * so whenever you extract an element from this list, you can use it
 * in position where a T is expected. You can read from this list.
 *
 * ** The exact type is unknown, so you can never be certain that an
 * instance of a particular subtype of T can be added to this list.
 * That is, you effectively cannot add new elements to such a list
 * (unless you use nulls / type casts / exploit unsoundness of Java's type system, that is)
 */

public class AppendProtectedLists {

    /*
    * In combination, it means that List<? extends T> is kind-of like an append-protected list,
    * with type-level append-protection
    */

    /**
     * A method that returns an empty append-protected list of given type:
     */
    public static <T> List<? extends  T> empty() {
        return new LinkedList<>();
    }

    /**
     * A append-protected list with a single element
     */
    public static <T> List<? extends  T> pure(T t) {
        List<T> result = new LinkedList<>();
        result.add(t);
        return result;
    }

    /**
     * A append-protected list from ordinary list
     */
    public static <T> List<? extends  T> toAppendProtected(List<T> original) {
        return new LinkedList<>(original);
    }

    /**
     * You can combine append-protected lists
     */
    public static <T> List<? extends T> combine(List<? extends T> a, List<? extends T> b) {
        List<T> result = new LinkedList<>();
        result.addAll(a);
        result.addAll(b);
        return result;
    }

    /**
     * Together combine() and empty() forms an algebraic structure --- a Monoid
     * Methods like pure() ensure that its --non-degenerate i.e., it has more elements
     * than just an empty list.. Indeed you had an interface similar to usual
     * Monoid type subclass:
     *
     * static interface Monoid<X> {
     *     X empty();
     *     X combine (X a, X b);
     * }
     *
     * And can be implemented as
     */

    public interface Monoid<X> {
        X empty();
        X combine(X a, X b);
    }

    public static <T> Monoid<List<? extends  T>> appendProtectedListsMonoid() {
        return new Monoid<>() {
            @Override
            public List<? extends T> empty() {
                return AppendProtectedLists.empty();
            }

            @Override
            public List<? extends T> combine(List<? extends T> a, List<? extends T> b) {
                return AppendProtectedLists.combine(a, b);
            }
        };
    }

    public static void main(String[] args) {
        Monoid<List<? extends String>> monoid = appendProtectedListsMonoid();
        List<? extends String> e = monoid.empty();
        List<? extends String> a = pure("a");
        List<? extends String> b = pure("b");
        List<? extends Integer> c = pure(1);
        List<? extends String> d = monoid.combine(e, monoid.combine(a, b));
        System.out.println(d);// [a ,b]
        System.out.println(c);// [1]
        System.out.println((long) c.size());
    }
}
